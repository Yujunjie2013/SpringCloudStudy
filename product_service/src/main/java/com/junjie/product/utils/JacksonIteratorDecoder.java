package com.junjie.product.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static feign.Util.ensureClosed;

public class JacksonIteratorDecoder implements Decoder {
    private final ObjectMapper mapper;

    JacksonIteratorDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.body() == null)
            return null;
        Reader reader = response.body().asReader(Util.UTF_8);
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader, 1);
        }
        try {
            // Read the first byte to see if we have any data
            reader.mark(1);
            if (reader.read() == -1) {
                return null; // Eagerly returning null avoids "No content to map due to end-of-input"
            }
            reader.reset();
            return new JacksonIterator<Object>(actualIteratorTypeArgument(type), mapper, response,
                    reader);
        } catch (RuntimeJsonMappingException e) {
            if (e.getCause() != null && e.getCause() instanceof IOException) {
                throw IOException.class.cast(e.getCause());
            }
            throw e;
        }
    }

    private static Type actualIteratorTypeArgument(Type type) {
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Not supported type " + type.toString());
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (!Iterator.class.equals(parameterizedType.getRawType())) {
            throw new IllegalArgumentException(
                    "Not an iterator type " + parameterizedType.getRawType().toString());
        }
        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    public static JacksonIteratorDecoder create() {
        return create(Collections.<Module>emptyList());
    }

    public static JacksonIteratorDecoder create(Iterable<Module> modules) {
        return new JacksonIteratorDecoder(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModules(modules));
    }

    public static JacksonIteratorDecoder create(ObjectMapper objectMapper) {
        return new JacksonIteratorDecoder(objectMapper);
    }

    static final class JacksonIterator<T> implements Iterator<T>, Closeable {
        private final Response response;
        private final JsonParser parser;
        private final ObjectReader objectReader;

        private T current;

        JacksonIterator(Type type, ObjectMapper mapper, Response response, Reader reader)
                throws IOException {
            this.response = response;
            this.parser = mapper.getFactory().createParser(reader);
            this.objectReader = mapper.reader().forType(mapper.constructType(type));
        }

        @Override
        public boolean hasNext() {
            if (current == null) {
                current = readNext();
            }
            return current != null;
        }

        private T readNext() {
            try {
                JsonToken jsonToken = parser.nextToken();
                if (jsonToken == null) {
                    return null;
                }

                if (jsonToken == JsonToken.START_ARRAY) {
                    jsonToken = parser.nextToken();
                }

                if (jsonToken == JsonToken.END_ARRAY) {
                    ensureClosed(this);
                    return null;
                }

                return objectReader.readValue(parser);
            } catch (IOException e) {
                // Input Stream closed automatically by parser
                throw new DecodeException(response.status(), e.getMessage(), response.request(), e);
            }
        }

        @Override
        public T next() {
            if (current != null) {
                T tmp = current;
                current = null;
                return tmp;
            }
            T next = readNext();
            if (next == null) {
                throw new NoSuchElementException();
            }
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() throws IOException {
            ensureClosed(this.response);
        }
    }
}

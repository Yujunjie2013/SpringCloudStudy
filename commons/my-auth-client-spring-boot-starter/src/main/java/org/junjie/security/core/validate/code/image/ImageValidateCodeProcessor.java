package org.junjie.security.core.validate.code.image;

import org.junjie.security.core.validate.code.ImageCode;
import org.junjie.security.core.validate.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * 图形验证码处理器
 */
@Component("imageValidateCodeProcessor")
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {
    /**
     * 发送图形验证码，将其写到响应中
     *
     * @param request      请求
     * @param validateCode 验证码
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode validateCode) throws IOException {
        ImageIO.write(validateCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}

/**
 *
 */
package org.junjie.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 校验码处理器管理器
 */
@Component
public class ValidateCodeProcessorHolder {

    /**
     * 依赖搜索
     *
     * Spring启动时，会查找容器中所有的ValidateCodeProcessor接口的实现，并把Bean的名字作为key，放到map中
     * 这里ValidateCodeProcessor有两个默认实现，ImageValidateCodeProcessor、SmsValidateCodeProcessor,通过@Component指定注册的名称
     *
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    /**
     *根据类型找到不同的验证码处理器，我这里默认提供了2个处理器，分别是smsValidateCodeProcessor和、imageValidateCodeProcessor
     * {@link org.junjie.security.core.validate.code.sms.SmsValidateCodeProcessor} 短信验证码处理器
     * {@link org.junjie.security.core.validate.code.image.ImageValidateCodeProcessor} 图形验证码处理器
     * @param type 这里的type默认的有sms/image
     * @return 验证码处理器
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }

}

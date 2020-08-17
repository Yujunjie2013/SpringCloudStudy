package org.junjie.security.core.validate.code.image;

import org.junjie.security.core.properties.SecurityProperties;
import org.junjie.security.core.validate.code.ImageCode;
import org.junjie.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageCodeGenerator implements ValidateCodeGenerator {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ImageCode generate(ServletWebRequest servletWebRequest) {
        return createImageCode(servletWebRequest.getRequest());
    }

    private ImageCode createImageCode(HttpServletRequest httpServletRequest) {
        int width = ServletRequestUtils.getIntParameter(httpServletRequest, "width", securityProperties.getCode().getImage().getWidth()); // 验证码图片宽度
        int height = ServletRequestUtils.getIntParameter(httpServletRequest, "height", securityProperties.getCode().getImage().getHeight()); // 验证码图片高度
        int length = securityProperties.getCode().getImage().getLength(); // 验证码位数
        int expireIn = securityProperties.getCode().getImage().getExpireIn(); // 验证码有效时间 60s

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        int x = (width - 20) / length;
        int y = (height + 15) / 2;
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, x * i + 10, y);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), expireIn);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}

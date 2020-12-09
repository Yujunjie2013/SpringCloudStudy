1、获取短信验证码和图形验证码地址
http://localhost:8090/code/image
http://localhost:8090/code/sms?mobile=13526880238
所有跟验证码相关的接口验证都必须要在请求头携带deviceId参数，sms短信需要手机号mobile,具体实现在ValidateCodeController中

2、获取Token
http://localhost:8090/oauth/token
Basic Auth
test
test

grant_type:password
username:asdf
password:123456
scope:read_userinfo read_contacts
scope有多个时用英文空格区分








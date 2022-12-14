package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.reggie.common.Result;
import com.reggie.pojo.User;
import com.reggie.service.UserService;
import com.reggie.utils.SMSUtils;
import com.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        String key = "code_" + phone;

        if (StringUtils.isNotEmpty(phone)) {
            //生成4位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code: {}", code);

            //调用aliyun提供的短信服务api完成短信的发送
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            //将生成的验证码保存到session
            //session.setAttribute(phone, code);

            //将生成的验证码保存到redis中
            redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

            return Result.success("手机验证码发送成功");
        }

        return Result.error("验证码发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map, HttpServletRequest request) {
        log.info("phone: {}, code: {}", map.get("phone"), map.get("code"));

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        String key = "code_" + phone;

        //从session中获取保存的验证码
        //String codeInSession = session.getAttribute("code").toString();

        //从redis中获取验证码
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);

        if (codeInRedis != null && codeInRedis.equals(code)) {
            //如果对比成功，则登录成功
            //判断当前手机号是否为新用户，如果是新用户，则自动完成注册
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            request.getSession().setAttribute("user", user.getId());

            redisTemplate.delete(key);
            return Result.success(user);
        }

        return Result.error("登录失败");
    }
}

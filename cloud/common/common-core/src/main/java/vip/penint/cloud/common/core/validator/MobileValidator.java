package vip.penint.cloud.common.core.validator;

import vip.penint.cloud.common.core.annotation.IsMobile;
import vip.penint.cloud.common.core.entity.constant.RegexpConstant;
import vip.penint.cloud.common.core.utils.PenintUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否为合法的手机号码
 *
 * 
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

    @Override
    public void initialize(IsMobile isMobile) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StringUtils.isBlank(s)) {
                return true;
            } else {
                String regex = RegexpConstant.MOBILE;
                return PenintUtil.match(regex, s);
            }
        } catch (Exception e) {
            return false;
        }
    }
}

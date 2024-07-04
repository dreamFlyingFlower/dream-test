package dream.flying.flower.framework.web.valid;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * 在进行通用增删改查的时候,校验新增参数的标识
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:52:39
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@GroupSequence({ ValidAdd.class, Default.class })
public interface ValidAdds {

}
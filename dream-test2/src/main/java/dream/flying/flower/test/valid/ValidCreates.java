package dream.flying.flower.test.valid;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 *  在进行通用增删改查的时候,校验新增参数的标识
 * @author ParadiseWY
 * @date 2019年7月31日 上午9:38:20
 */
@GroupSequence({ ValidCreate.class, Default.class })
public interface ValidCreates {

}
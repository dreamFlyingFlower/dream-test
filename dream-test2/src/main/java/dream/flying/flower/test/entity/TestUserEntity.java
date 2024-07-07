package dream.flying.flower.test.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.ConstDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试实体类
 *
 * @author 飞花梦影
 * @date 2024-07-04 20:58:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestUserEntity {

	private Long id;

	private String username;

	private String password;

	private BigDecimal salary;

	@DateTimeFormat(pattern = ConstDate.DATETIME)
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date createTime;
}
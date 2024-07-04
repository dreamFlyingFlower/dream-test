package dream.flying.flower.framework.web.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import dream.flying.flower.ConstDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 操作日志类
 *
 * @author 飞花梦影
 * @date 2022-11-12 21:28:30
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperateLog {

	/** 日志主键 */
	// @Excel(name = "操作序号", cellType = ColumnType.NUMERIC)
	private Long id;

	/** 操作模块 */
	// @Excel(name = "操作模块")
	private String moduleName;

	/** 业务类型（0其它 1新增 2修改 3删除） */
	// @Excel(name = "业务类型", readConverterExp =
	// "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
	private Integer businessType;

	/** 业务类型数组 */
	private Integer[] businessTypes;

	/** 请求方法名 */
	// @Excel(name = "请求方法名")
	private String methodName;

	/** 请求方式 */
	// @Excel(name = "请求方式")
	private String requestMethod;

	/** 操作类别（0其它 1后台用户 2手机端用户） */
	// @Excel(name = "操作类别", readConverterExp = "0=其它,1=后台用户,2=手机端用户")
	private Integer operateType;

	/** 操作人员 */
	// @Excel(name = "操作人员")
	private String operator;

	/** 请求url */
	// @Excel(name = "请求地址")
	private String operateUrl;

	/** 操作地址 */
	// @Excel(name = "操作地址")
	private String operateIp;

	/** 请求参数 */
	// @Excel(name = "请求参数")
	private String operateParam;

	/** 返回参数 */
	// @Excel(name = "返回参数")
	private String jsonResult;

	/** 操作状态(0-正常;1-异常) */
	// @Excel(name = "状态", readConverterExp = "0=正常,1=异常")
	private Integer status;

	/** 错误消息 */
	// @Excel(name = "错误消息")
	private String errorMsg;

	/**
	 * 开始执行时间
	 */
	@Schema(description = "开始执行时间")
	private Date beginTime;

	/** 操作时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private Long operateTime;

	/**
	 * 结束执行时间
	 */
	@Schema(description = "结束执行时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	@DateTimeFormat(pattern = ConstDate.DATETIME)
	private Date endTime;

	/**
	 * 创建者
	 */
	private Long createUser;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = ConstDate.DATETIME)
	@DateTimeFormat(pattern = ConstDate.DATETIME)
	private Date createTime;

	/** 更新者 */
	private Long updateUser;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = ConstDate.DATETIME)
	@DateTimeFormat(pattern = ConstDate.DATETIME)
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;
}
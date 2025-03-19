//package dream.flying.flower.test.controller;
//
//import java.util.Date;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import dream.flying.flower.autoconfigure.cryption.annotation.CryptionController;
//import dream.flying.flower.digest.DigestHelper;
//import dream.flying.flower.result.Result;
//import dream.flying.flower.test.entity.TestUserEntity;
//
///**
// * 测试登录到gitee之后获取用户信息
// *
// * @author 飞花梦影
// * @date 2024-07-04 20:59:28
// * @git {@link https://github.com/dreamFlyingFlower}
// */
//@CryptionController
//@RequestMapping("testGitee")
//@RestController
//public class TestGiteeController {
//
//	@GetMapping("info")
//	public Result<?> test() {
//		String name = SecurityContextHolder.getContext().getAuthentication().getName();
//		System.out.println(SecurityContextHolder.getContext().getAuthentication());
//		System.out.println(name);
//		return Result.ok(TestUserEntity.builder()
//				.id(10000L)
//				.username("测试下")
//				.password(DigestHelper.uuid())
//				.createTime(new Date())
//				.build());
//	}
//}
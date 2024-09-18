package dream.flying.flower.test.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dream.flying.flower.autoconfigure.cryption.annotation.CryptionController;
import dream.flying.flower.autoconfigure.cryption.annotation.DecryptRequest;
import dream.flying.flower.autoconfigure.cryption.annotation.EncryptResponse;
import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.result.Result;
import dream.flying.flower.test.entity.TestUserEntity;

/**
 * 测试RequestBoby和ResponseBody加解密
 *
 * @author 飞花梦影
 * @date 2024-07-04 20:59:28
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@CryptionController
@RequestMapping("testValid")
@RestController
public class TestValidController {

	@GetMapping("test-result-encrypt")
	@EncryptResponse
	public Result<?> testResultEncrypt() {
		return Result.ok(TestUserEntity.builder().id(10000L).username("测试下").password(DigestHelper.uuid())
				.createTime(new Date()).build());
	}

	@PostMapping("test-result-decrypt")
	@DecryptRequest
	public Result<?> testResultDecrypt(@RequestBody Result<TestUserEntity> result) {
		return Result.ok();
	}
}
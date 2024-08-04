package com.wy.test.core.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserApps extends AppVO {

	private static final long serialVersionUID = 5100063509378932520L;

	private String username;

	private String userId;

	private String displayName;
}
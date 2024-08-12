package com.wy.test.core.entity.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameEntity {

	private String givenName;

	private String middleName;

	private String familyName;

	private String honorificPrefix;

	private String honorificSuffix;

	private String formattedName;
}
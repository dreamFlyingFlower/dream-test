package com.wy.test.synchronizer.workweixin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkWeixinDepts {

	long id;

	String name;

	String name_en;

	long parentid;

	Integer order;
}
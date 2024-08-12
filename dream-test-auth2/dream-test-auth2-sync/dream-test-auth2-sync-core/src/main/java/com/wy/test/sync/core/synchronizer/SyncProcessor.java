package com.wy.test.sync.core.synchronizer;

import com.wy.test.core.entity.SyncEntity;

public interface SyncProcessor {

	void sync() throws Exception;

	void setSynchronizer(SyncEntity syncEntity);
}
package com.wy.test.sync.core.synchronizer;

import com.wy.test.core.entity.SyncEntity;

public interface ISynchronizerService {

	public void sync() throws Exception;

	public void setSynchronizer(SyncEntity synchronizer);
}

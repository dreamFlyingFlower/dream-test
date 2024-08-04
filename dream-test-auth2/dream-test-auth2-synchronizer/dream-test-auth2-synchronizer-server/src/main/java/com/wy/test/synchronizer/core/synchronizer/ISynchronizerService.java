package com.wy.test.synchronizer.core.synchronizer;

import com.wy.test.core.entity.SyncEntity;

public interface ISynchronizerService {

	public void sync() throws Exception;

	public void setSynchronizer(SyncEntity synchronizer);
}

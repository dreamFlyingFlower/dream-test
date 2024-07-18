package com.wy.test.synchronizer.core.synchronizer;

import com.wy.test.core.entity.Synchronizers;

public interface ISynchronizerService {

	public void sync() throws Exception;

	public void setSynchronizer(Synchronizers synchronizer);
}

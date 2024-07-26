package com.wy.test.persistence.provision.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProvisioningRunnerThread extends Thread {

	private static final Logger _logger = LoggerFactory.getLogger(ProvisioningRunnerThread.class);

	ProvisioningRunner runner;

	public ProvisioningRunnerThread(ProvisioningRunner runner) {
		super();
		this.runner = runner;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60 * 1000);
				_logger.trace("Provisioning start ...");
				runner.provisions();
				_logger.trace("Provisioning end , wait for next .");
			} catch (InterruptedException e) {
				_logger.error("InterruptedException", e);
			}
		}
	}
}

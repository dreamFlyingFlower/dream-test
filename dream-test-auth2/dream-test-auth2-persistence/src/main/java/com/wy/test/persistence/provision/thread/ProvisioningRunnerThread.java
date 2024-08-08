package com.wy.test.persistence.provision.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProvisioningRunnerThread extends Thread {

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
				log.trace("Provisioning start ...");
				runner.provisions();
				log.trace("Provisioning end , wait for next .");
			} catch (InterruptedException e) {
				log.error("InterruptedException", e);
			}
		}
	}
}
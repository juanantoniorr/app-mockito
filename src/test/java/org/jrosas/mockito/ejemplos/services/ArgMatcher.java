package org.jrosas.mockito.ejemplos.services;

import org.mockito.ArgumentMatcher;

public class ArgMatcher implements ArgumentMatcher<Long> {
	

	@Override
	public boolean matches(Long argument) {
		
		return argument != null && argument > 0;
		
		
	}

}

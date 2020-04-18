package com.dyteam.testApps.webserver.model;

import java.io.Serializable;

public class CommonModel<K,V> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3291085917765533663L;
	public K key;
	public V value;
	
}

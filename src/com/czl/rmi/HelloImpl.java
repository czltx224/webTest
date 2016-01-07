package com.czl.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello {

	private static final long serialVersionUID = 1L;

	public HelloImpl() throws RemoteException {
		super();
	}

	@Override
	public String sayHello(String name) throws RemoteException {
		return "Welcome, " + name;
	}

	@Override
	public int sum(int a, int b) throws RemoteException{
		return a + b;
	}
}
package com.almundo.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.almundo.dispatch.PriorityCallDispatcher;
import com.almundo.model.Call;
import com.almundo.model.Employee.EmployeeType;

class PriorityCallDispatcherTest {
	PriorityCallDispatcher dispatcher;
	
	@Before
	void setup() {
	}

	
	/*Esta prueba muestra como el sistema toma con prioridad a
	 *  los operadores como próximos en ser asignados para manejar una llamada*/
	
	@Test
	void testDispatchCallWith10CallsAndMoreEmployees() {
		int callCenterCapacity = 10;
		int operatorsCount = 10;
		int supervisorsCount = 5;
		int directorsCount = 5;
		
		int callsCount = 10;
		dispatcher = new PriorityCallDispatcher(callCenterCapacity,operatorsCount,supervisorsCount,directorsCount);
		ArrayList<Call> calls =  new ArrayList<>(callsCount);

		for(int i=0; i<callsCount;i++) {
			Long duration = 5000 + (long) (Math.random() * (10000 - 5000));
			calls.add(new Call(duration, dispatcher));
		}
		
		for(Call call:calls) {
			dispatcher.dispatchCall(call);
		}
		while((dispatcher.getFinishedCallsCount())<callsCount) {
			System.out.println(dispatcher.getFinishedCallsCount());//Print para ver que el proceso avanza (ayuda visual unicamente)
			try {
				Thread.sleep(1000);//en lugar de revisar constantemente se revisa cada segundo para ver si ya terminó el proceso
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		calls = dispatcher.getFinishedCalls();
		for(Call call:calls) {
			assertTrue(call.getHandler().getEmployeeType() == EmployeeType.OPERATOR);
		}
	}
	
	/*Esta prueba muestra como el sistema toma con prioridad a
	 *  los operadores como próximos en ser asignados para manejar una llamada*/
	@Test
	void testDispatchCallWith10CallsAnd10Employees() {
		int callCenterCapacity = 10;
		int operatorsCount = 4;
		int supervisorsCount = 3;
		int directorsCount = 3;
		
		int callsCount = 10;
		dispatcher = new PriorityCallDispatcher(callCenterCapacity,operatorsCount,supervisorsCount,directorsCount);
		
		ArrayList<Call> calls =  new ArrayList<>(callsCount);
		

		for(int i=0; i<callsCount;i++) {
			Long duration = 5000 + (long) (Math.random() * (10000 - 5000));
			calls.add(new Call(duration, dispatcher));
		}
		
		for(Call call:calls) {
			dispatcher.dispatchCall(call);
		}
		while(dispatcher.getFinishedCallsCount()<callsCount) {
			System.out.println(dispatcher.getFinishedCallsCount());//Print para ver que el proceso avanza (ayuda visual unicamente)
			try {
				Thread.sleep(1000);//en lugar de revisar constantemente se revisa cada segundo para ver si ya terminó el proceso
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		calls = dispatcher.getFinishedCalls();
		
		for(int i = 0; i<operatorsCount;i++) {
			assertTrue(calls.get(i).getHandler().getEmployeeType()==EmployeeType.OPERATOR);
		}
		for(int i = operatorsCount; i<operatorsCount+supervisorsCount;i++) {
			assertTrue(calls.get(i).getHandler().getEmployeeType()==EmployeeType.SUPERVISOR);
		}
		for(int i = supervisorsCount+operatorsCount; i<supervisorsCount+directorsCount;i++) {
			assertTrue(calls.get(i).getHandler().getEmployeeType()==EmployeeType.DIRECTOR);
		}
	}
	

	/*Esta prueba muestra como el sistema sin importar cuantos empleados tenga
	 *  procesará todas las llamadas entrantes*/
	
	@Test
	void testDispatchCallWith20CallsAnd10Employees() {
		int callCenterCapacity = 10;
		int operatorsCount = 5;
		int supervisorsCount = 3;
		int directorsCount = 2;
		
		int callsCount = 20;
		dispatcher = new PriorityCallDispatcher(callCenterCapacity,operatorsCount,supervisorsCount,directorsCount);
		ArrayList<Call> calls =  new ArrayList<>(callsCount);

		for(int i=0; i<callsCount;i++) {
			Long duration = 5000 + (long) (Math.random() * (10000 - 5000));
			calls.add(new Call(duration, dispatcher));
		}
		
		for(Call call:calls) {
			dispatcher.dispatchCall(call);
		}
		while((dispatcher.getFinishedCallsCount())<callsCount) {
			System.out.println(dispatcher.getFinishedCallsCount());//Print para ver que el proceso avanza (ayuda visual unicamente)
			try {
				Thread.sleep(1000);//en lugar de revisar constantemente se revisa cada segundo para ver si ya terminó el proceso
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		calls = dispatcher.getFinishedCalls();
		assertTrue(calls.size()==callsCount);
	}
	
	
	

}

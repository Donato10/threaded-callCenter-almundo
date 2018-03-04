package com.almundo.dispatch;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import com.almundo.GUI.Simulation;
import com.almundo.model.Call;
import com.almundo.model.Director;
import com.almundo.model.Employee;
import com.almundo.model.Operator;
import com.almundo.model.Supervisor;

/* Implementación de dispatcher por prioridad*/
public class PriorityCallDispatcher {
	
	
	ExecutorService callAllocator;   //procesador de llamadas, representa las lineas (capacidad) del callcenter 
	private  ArrayList<Employee> callHandlers; //Lista de empleados del call center, está lista no cambia. 
	private  Queue<Employee> availableCallHandlers; //Lista de empleados disponibles en el sistema
	private  ArrayList<Call> finishedCalls= new ArrayList<>(); //Lista de llamadas finalizadas, sirve para guardar el historico procesado por el sistema como parte de la simulación
	
	public int getFinishedCallsCount() {
		return finishedCalls.size();
	}
	
	public ArrayList<Call> getFinishedCalls(){
		ArrayList<Call> clone = new ArrayList<>(finishedCalls.size());
		//Comparador inicial de ordenamiento por fecha de creación de la llamada
		Comparator<Call> sortByCreationTime = (call1, call2) -> call1.getInitTime().compareTo(call2.getInitTime());
		//En caso de que dos llamadas ingresen al mismo tiempo exactamente, se comparará el tipo de empleado que la atendió
		Comparator<Call> sortByHanlderType = (call1, call2) -> call1.getHandler().getEmployeeType().getTypeValue().compareTo(call2.getHandler().getEmployeeType().getTypeValue());
		finishedCalls
		.stream()
		.sorted(
				sortByCreationTime
					.thenComparing(sortByHanlderType)
			   )
		.forEach(
				call->
					clone.add(call)
				);		
		return clone;
	
	}	

	
	synchronized Employee waitForAvailability() {
		Employee emp = null;
		try {
				while((emp=availableCallHandlers.poll())==null) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emp;

	}
	
	
	public synchronized void finishCall(Call call) {
			availableCallHandlers.add(call.getHandler());
			this.finishedCalls.add(call);
			Simulation.addAttendedCall(call);
			notify();
	}

	/*Constructor que recibe:
	 *  La cantidad de líneas del sistema, 
	 *  La cantidad de operadores
	 *  La cantidad de supervisores
	 *  La cantidad de directores.
	 *  Generá una cola thread safe con prioridad
	 *   que se encarga de retornar el empleado que debe atender la llamada entrante
	 *  	En este punto tambien se inicializa la representación gráfica de los mpleados en la simulación*/
	public PriorityCallDispatcher(Integer callCenterCapacity, Integer operatorsCount,
			Integer supervisorsCount, Integer directorsCount) {
		this.callAllocator =  Executors.newFixedThreadPool(callCenterCapacity);//Generación de las lineas de llamada, con capacidad máxima
		callHandlers = new ArrayList<Employee>(operatorsCount + supervisorsCount + directorsCount); //Lista de empleados del CallCenter
		availableCallHandlers = new PriorityBlockingQueue<Employee>(operatorsCount + supervisorsCount + directorsCount);//Lista con prioridad que guardará solamente los empleados disponibles ene le momento
		
		//Instanciación de empleados
		for(int i = 0;i < Math.max(Math.max(operatorsCount, supervisorsCount), directorsCount); i++) {
			if(i < supervisorsCount)
				callHandlers.add(new Supervisor());
			if(i < directorsCount)
				callHandlers.add(new Director());
			if(i < operatorsCount)
				callHandlers.add(new Operator());
		}
		//Inicialización de la representación gráfica de los empleados del sistema
		Simulation.handlers = callHandlers;
		availableCallHandlers.addAll(callHandlers);//Inicialmenete todos los empleados se consideran disponibles
	}


	public  void dispatchCall(Call call) {
		//Se obtiene el empleado con mayor prioridad para atender la llamada
		Employee emp = availableCallHandlers.poll();
		//Si el empleado existe se asigna a la llamada, de otra forma, la llamada se rechaza,
		//solamente se rechazan llamadas cuando la llamada entra al sistema pero no hay empleados disponibles
		if(emp==null){
			emp=waitForAvailability();
		}
		call.setHandler(emp);
		callAllocator.execute(call);
	}
}

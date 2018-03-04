package com.almundo.model;


import java.time.LocalDateTime;

import com.almundo.dispatch.PriorityCallDispatcher;
import com.almundo.model.Employee.EmployeeStatus;

public class Call implements Runnable{

	private Employee handler; //Empleado que atenderá la llamada 
	private Long duration;    //Duración de la llamada, permitirá calcular metricas
	private LocalDateTime creationTime; //Momento en que la llamada es generada
	private LocalDateTime initTime; //Momento en que la llamada empieza a ser procesada por un empleado
	private PriorityCallDispatcher dispatcher; //Dispatcher asociado 
	
	

	public Call(Long duration, PriorityCallDispatcher dispatcher) {
		this.duration = duration;
		this.creationTime = LocalDateTime.now();
		this.dispatcher = dispatcher;
	}
	
	
	//Grupo de métodos de acceso y asignación
	public Employee getHandler() {
		return handler;
	}

	public void setHandler(Employee handler) {
		this.handler = handler;
		this.handler.setEmployeeStatus(EmployeeStatus.BUSY);
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getInitTime() {
		return initTime;
	}

	public void setInitTime(LocalDateTime initTime) {
		this.initTime = initTime;
	}
	

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}
	

	@Override
	public String toString() {
		return "Call [handler=" + handler + ", duration=" + duration + ", creationTime=" + creationTime + ", initTime="
				+ initTime + "]";
	}


	//Debido a que esta clase implementa la interfaz Runnable es necesario 
	//sobre-escribir el método run() que se encargará de simular la atención de la llamada
	@Override
	public void run() {
		try {
			this.initTime = LocalDateTime.now();//Se guarda el instante donde la llamada empieza a ser atendida
			Thread.sleep(this.duration);//La simula la duración de la llamada
			this.handler.setEmployeeStatus(EmployeeStatus.AVAILABLE);//La llamada ha finalizado, el empleado está libre ahora
			Thread.sleep(200L);	//Breve instante agregado por conveniencia visual ya que de no existir no sería sencillo ver el cambio de estado en la simulación
			dispatcher.finishCall(this);//El empleado ahora está disponible para atender mas llamadas, y la llmada finalizada se agrega a los resultados

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

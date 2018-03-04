package com.almundo.GUI;

import com.almundo.model.Employee;
import com.almundo.model.Employee.EmployeeStatus;

import processing.core.PApplet;


/* Representacin gráfica de cada uno de los empleados que pueden atender una llamada*/
public class Handler {

	float x; //Coordenada horizontal del objeto en el espacio grafico de la simulación
	float y; //Coordenada vertical del objeto en el espacio grafico de la simulación

	PApplet parent; //Padre (basicamente este padré es la simulación)
	Employee associatedHandler; //El empleado "lógico" asociado; el empleado que es representado 
	
	public Handler(PApplet simulation, Employee handler, float xCoordinate, float yCoordinate){
		this.parent = simulation;
		this.associatedHandler = handler;
		this.x =xCoordinate;
		this.y = yCoordinate;
	}

	/*Método encargado de pintar el objeto gráfico en la simulación*/
	void display(){
		parent.fill(255,255,255); // inicialmente se escoge color blanco como valor por defecto  
		if( associatedHandler.getEmployeeStatus() == EmployeeStatus.AVAILABLE){
			parent.fill(55,255,55); //Si el empleado está disponible se asigna verde como color 
		}
		else if(associatedHandler.getEmployeeStatus() == EmployeeStatus.BUSY){
			parent.fill(255,55,55); //Si el empleado está ocupado será tojo el color 
		}
		
		//Gracias a que enum es un tipo de dato "TypeSafe" podemos utilizar la estructura de control
		// para variar el tipo de figura geometrica utilizada por tipo de empleado
		switch(associatedHandler.getEmployeeType()){
			case DIRECTOR:
				parent.rect(x,y,Simulation.HANDLER_SIZE[0], Simulation.HANDLER_SIZE[1]);//Rectangulo
				break;
			case OPERATOR:
				parent.triangle( x+(Simulation.HANDLER_SIZE[0]/2), y+Simulation.HANDLER_SIZE[1], x,y , x+Simulation.HANDLER_SIZE[0], y);//Triangulo
				break;
			case SUPERVISOR:
				parent.ellipse( x+Simulation.HANDLER_SIZE[0]/2, y+Simulation.HANDLER_SIZE[1]/2, Simulation.HANDLER_SIZE[0], Simulation.HANDLER_SIZE[1]);//Circulo
				break;
			default:
				break;
			}
	}

}

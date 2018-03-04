package com.almundo.GUI;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import com.almundo.dispatch.PriorityCallDispatcher;
import com.almundo.model.Call;
import com.almundo.model.Employee;

import processing.core.PApplet;
import processing.core.PFont;

public class Simulation extends PApplet {

	//Métricas actualizadas en vivo durante la simulación 
	private static float avgWaitingTime;
	private static float avgDuration;
	private static int attendedCalls;
	
	private static int expectedCalls;
	
	
	//Propiedades del boton de finalización
	private boolean overEndButton;
	private int endButtonX;
	private int endButtonY;
	private int endButtonWidth;
	private int endButtonHeight;
	
	public static int getExpectedCalls() {
		return expectedCalls;
	}

	public static void setExpectedCalls(int expectedCalls) {
		Simulation.expectedCalls = expectedCalls;
	}

	public static final float[] HANDLER_SIZE = {50F,50F}; //Propiedades de simulación para los empleados (gráficos)
	
	private static final float HANDLER_HORIZONTAL_SEPARATION = 20F; //Propiedades de simulación
	private static final float HANDLER_VERTICAL_SEPARATION = 30F; //Porpiedadesde simulación
	
	public static ArrayList<Employee> handlers; //Empleados del sistema
	private ArrayList<Handler> handlersGUI = new ArrayList<>(); //Representación gráfica de los empleados del sistema


	//Propiedades de simulación
	private PFont textPainterFont;
	private Float nextHandlerXCoordinate = -50F;
	private Float nextHandlerYCoordinate = -50F;
	private boolean verticalJumpNeeded = true;


		//Método utilizado para dictar propiedades generales de la simulación (Propio de processing)
	    public void settings(){
		  fullScreen();
	    }

	    	//Método que generá las representaciones gráficas de los empleados del sistema
	    public void setup(){
	    	    overEndButton = false;
	    		endButtonX = 600;
	    		endButtonY = displayHeight-150;
	    		endButtonWidth = 300;
	    		endButtonHeight = 100;
		    	textPainterFont = createFont("Arial", 16, true);
		    	for(Employee employee:handlers){
		    		handlersGUI.add(new Handler(this, employee, getNextHandlerXCoordinate(), getNextHandlerYCoordinate()));
			}
		    	 background(0);
		    	  for(Handler handler:handlersGUI){
		    		  handler.display();
		    	  }
		    	  refreshWaitingTime();
		    	  refreshAVGDuration();
		    	  refreshAttendedCalls();
	    }
	    

	    
	    public static void addAttendedCall(Call call) {
		    	attendedCalls++;
			updateAVGWaitingTime(call);
			updateAVGDuration(call);
	    }


	    public void refreshWaitingTime() {
	    		textFont(textPainterFont,16);
	    		fill(255);
	    		text("Avg. Waiting time",10,displayHeight-50);
	    		textFont(textPainterFont,35);
	    		text(avgWaitingTime,15,displayHeight-100);
	    }

	    public void refreshAVGDuration() {
	    		textFont(textPainterFont,16);
	    		fill(255);
	    		text("Avg. Call duration",200,displayHeight-50);
	    		textFont(textPainterFont,35);
	    		text(avgDuration,215,displayHeight-100);
	    }



	    public void refreshAttendedCalls() {
		    	textFont(textPainterFont,16);
	    		fill(255);
	    		text("Attended Calls",400,displayHeight-50);
	    		textFont(textPainterFont,35);
	    		text(attendedCalls,415,displayHeight-100);
	    }

	    public void draw(){
	    	  background(0);
	    	  for(Handler handler:handlersGUI){
	    		  handler.display();
	    	  }
	    	  refreshWaitingTime();
	    	  refreshAVGDuration();
	    	  refreshAttendedCalls();
	    	  if(attendedCalls==expectedCalls) {
	    		  paintEndButton();
	    		  update(mouseX, mouseY);
	    	  }
	    }
	    
	    void update(int x, int y) {
	    	 if( overRect(endButtonX, endButtonY, endButtonWidth, endButtonHeight)) {
	    		 overEndButton = true;
	    	  } else {
	    		  overEndButton = false;
	    	  }
	    	}

	    private float getNextHandlerXCoordinate(){
		    	if((nextHandlerXCoordinate + HANDLER_SIZE[0]*2 + HANDLER_HORIZONTAL_SEPARATION) < displayWidth){
		    		this.nextHandlerXCoordinate += HANDLER_SIZE[0] + HANDLER_HORIZONTAL_SEPARATION;
		    	}
		    	else{
		    		nextHandlerXCoordinate = HANDLER_HORIZONTAL_SEPARATION;
		    		verticalJumpNeeded = true;
		    	}
	    	return nextHandlerXCoordinate;
	    }

	    private float getNextHandlerYCoordinate(){
		    	if(verticalJumpNeeded){
		    		this.nextHandlerYCoordinate += HANDLER_SIZE[1] + HANDLER_VERTICAL_SEPARATION;
		    		verticalJumpNeeded = false;
		    	}

	    	return nextHandlerYCoordinate;
	    }


	    private static void updateAVGWaitingTime(Call call) {
	    		long callWaitingTime = call.getCreationTime().until(call.getInitTime(), ChronoUnit.SECONDS);
	    		avgWaitingTime = (((attendedCalls-1)*avgWaitingTime)+callWaitingTime)/attendedCalls;
	    }


	    private static void updateAVGDuration (Call call) {
	    		avgDuration = (((attendedCalls-1)*avgDuration)+(call.getDuration())/1000)/attendedCalls;
	    }
	    
	    
	    public static void main(String[] args) {
	    	
	    		int lineCount;
	    		int operatorsCount;
	    		int supervisorsCount;
	    		int directorsCount;
	    		
	    		int callsCount;
	    		
	    		Scanner scanner = new Scanner(System.in);
	    		System.out.println("Bienvenido al sistema de simulación de call center de Nestor Donato,"
	    				+ " \npor favor tenga en cuenta lo siguiente y siga las instrucciones para ejecutar la simulación");
	    		
	    		System.out.println("\n\nModo de funcionamiento: "
	    				+ "\n * Si el número de líneas telefonicas el igual o menor al número de empleados, ninguna llamada será nunca rechazada. "
	    				+ "\n Las llamadas que no se puedan atender inmediatamente son puestas en espera.");
	    		
	    		System.out.println("\n * Si el número de líneas telefonicas es mayor al número de empleados es el mismo."
	    				+ "\n Las llamadas que ingresen en el sistema cuando ningún empleado esté libre pero si una línea serán inmediataente rechazadas.");	    		
	    		
	    		System.out.println("\n * Cada llamada tendrá una duración aleatoria entre 5 y 10 segundos");
	    		
	    		
	    		
	    		do{
	    			System.out.println("\n\nPor favor ingrese la cantidad de líneas telefonicas del sistema (1-100): ");
	    			lineCount =  scanner.nextInt();
	    		}while(lineCount <= 0 || lineCount >= 100);
	    		
	    		
	    		
	    		do{
	    			System.out.println("Por favor ingrese la cantidad de operadores del sistema (1-100): ");
	    			operatorsCount =  scanner.nextInt();
	    		}while(operatorsCount <= 0 || operatorsCount >= 100);

	    		
	    		do{
	    			System.out.println("Por favor ingrese la cantidad de Supervisores del sistema (1-100): ");
	    			supervisorsCount =  scanner.nextInt();
	    		}while(supervisorsCount <= 0 || supervisorsCount >= 100);
	    		
	    		
	    		do{
	    			System.out.println("Por favor ingrese la cantidad de Directores del sistema (1-100): ");
	    			directorsCount =  scanner.nextInt();
	    		}while(directorsCount <= 0 || directorsCount >= 100);
	    		
	    		
	    		do{
	    			System.out.println("Por favor ingrese la cantidad de llamadas a procesar ( > 0): ");
	    			callsCount =  scanner.nextInt();
	    		}while(callsCount <= 0 );
	    		
	    		System.out.println("Iniciando simulación.");
	    		scanner.close();
	    		
	    		PriorityCallDispatcher dispatcher = new PriorityCallDispatcher(lineCount, operatorsCount, supervisorsCount, directorsCount);
			Simulation.setExpectedCalls(callsCount);
			PApplet.main("com.almundo.GUI.Simulation");
	    		
	    		ArrayList<Call> calls =  new ArrayList<>(callsCount);

			for(int i=0; i<callsCount;i++) {
				Long duration = 5000 + (long) (Math.random() * (10000 - 5000));
				calls.add(new Call(duration, dispatcher));
			}
			
			for(Call call:calls) {
				dispatcher.dispatchCall(call);
			}

		}
	    
	    //revisa si el mouse está sobre el boton de finalizar simulación
	    boolean overRect(int x, int y, int width, int height)  {
	    	  if (mouseX >= x && mouseX <= x+width && 
	    	      mouseY >= y && mouseY <= y+height) {
	    		  cursor(HAND);
	    	    return true;
	    	  } else {
	    		  cursor(ARROW);
	    	    return false;
	    	  }
	    	}
	    
	    //pinta el boton de finalizar simulación
	    void paintEndButton(){
	    		fill(255);
	    		rect(endButtonX, endButtonY, endButtonWidth, endButtonHeight);
	    		textFont(textPainterFont,20);
	    		fill(0);
	    		text("Terminar Simulación",650,displayHeight-100);
	    }
	    
	    //método para procesar el click en el boton de finalizar simulación
	    public void mousePressed() {  	 
	    	  if (overEndButton) {
	    		  System.out.println("La simulación ha terminado, a continuación podrá ver los resultados: ");
	    		  System.out.println("TIEMPO PROMEDIO DE DURACIÓN POR LLAMADA: "+avgDuration);
	    		  System.out.println("TIEMPO PROMEDIO DE ESPERA POR LLAMADA (incluye las llamadas atenidas inmediatamente): "+avgWaitingTime);
	    		  exit();
	    	  }
	    	}
	    
}

package com.almundo.model;


/*Clase que representará cada uno de los posibles encargados de atender las llamadas, esta  
*  clase es abstracta debido a que aunque la definición de empleado es aplicable a todos los tipos de empleado, 
*  no debe existir una instancia de empleado sino de sus subtipos
*/
public abstract class Employee implements Comparable<Employee>{

	/*Método que permitirá comparar cada instancia de los subtipos de empleado
	 * para decidir cual empleado atenderá la siguiente llamada*/
	@Override
	public int compareTo(Employee employee) {
		/* Si el empleado y el comparable están ocupados se consideran con misma prioridad
		 * ya que ninguno podrá atender la llamada y esta estará en esperera o será rechazada directamente*/ 
		if(this.employeeStatus == employee.employeeStatus && this.employeeStatus == EmployeeStatus.BUSY) {
			return 0;
		}
		/* Si el empleado está disponible pero el comparable no, el empleado tiene mas prioridad*/
		if(employee.employeeStatus == EmployeeStatus.AVAILABLE && this.employeeStatus == EmployeeStatus.BUSY) {
			return 1;
		}
		/* Si el empleado está ocupado y no se ha retornado el resultado aun, 
		 * significa que el comparable está disponible y por lo tanto tiene mayor prioridad*/
		if(employee.employeeStatus == EmployeeStatus.BUSY) {
			return -1;
		}
		/* En este punto la unica opción es que tanto el empleado como el comparable estén
		 * disponibles, en este caso si son del mismo tipo tienen la misma prioridad*/
		if(employee.employeeType == this.employeeType) {
			return 0;
		}
		/*Si ambos tanto empleado como comparable están disponibles se regresará la
		 * resta de los valores asociados a su tipo es decir mayor prioridad para Operador,
		 * supervisor y por último director. De ser necesario se pueden agregar mas tipos
		 * de empleados y no será necesario reformar este método*/
		return (this.employeeType.getTypeValue() - employee.getEmployeeType().getTypeValue());
	}

	/* Sobre escritura del método equals para evitar
	 * NullPointersException durante la comparaciòn contra un objeto nulo
	 * y desde luego para garantizar el comportamiento adecuado en la 
	 * comparación de igualdad
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (employeeStatus != other.employeeStatus)
			return false;
		if (employeeType != other.employeeType)
			return false;
		return true;
	}

	/* ENUM: Decidí implementar un Enum en lugar de un simple booleano o un String
	 * para garantizar el control sobre los valores posibles y tambien permitir 
	 * la inclusin futura de diferentes opciones */
	public enum EmployeeStatus{
		AVAILABLE,
		BUSY;
	};

	public enum EmployeeType{
		OPERATOR,
		SUPERVISOR,
		DIRECTOR;
		public Integer getTypeValue() {
			switch(this) {
			case DIRECTOR:
				return 30;
			case OPERATOR:
				return 10;
			case SUPERVISOR:
				return 20;
			default:
				return 0;

			}
		}
	};


	public Employee( ) {
		this.employeeStatus = EmployeeStatus.AVAILABLE;
	};

	protected EmployeeStatus employeeStatus;
	protected EmployeeType employeeType;



	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	
	public EmployeeStatus getEmployeeStatus() {
		return employeeStatus;
	}

	public void setEmployeeStatus(EmployeeStatus employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	/* Sobreescritura del método para personalizar el resultado cuando
	 * se llame a imprimir una instancia de tipo employee*/
	@Override
	public String toString() {
		return "Employee [employeeStatus=" + employeeStatus + ", employeeType=" + employeeType +this.hashCode()+ "]";
	}





}



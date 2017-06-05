package asgn2Pizzas;

import java.time.LocalTime;
import asgn2Exceptions.PizzaException;

/**
 * An abstract class that represents pizzas sold at the Pizza Palace restaurant. 
 * The Pizza class is used as a base class of VegetarianPizza, MargheritaPizza and MeatLoversPizza. 
 * Each of these subclasses have a different set of toppings. A description of the class's fields
 * and their constraints is provided in Section 5.1 of the Assignment Specification. 
 * 
 * @author Joshua Serong
 *
 */
public abstract class Pizza  {
	
	/**
	 *  This class represents a pizza produced at the Pizza Palace restaurant.  A detailed description of the class's fields
	 *  and parameters is provided in the Assignment Specification, in particular in Section 5.1. 
	 *  A PizzaException is thrown if the any of the constraints listed in Section 5.1 of the Assignment Specification
	 *  are violated. 
     *
     *  PRE: TRUE
	 *  POST: All field values except cost per pizza are set
	 * 
	 * @param quantity - The number of pizzas ordered 
	 * @param orderTime - The time that the pizza order was made and sent to the kitchen 
	 * @param deliveryTime - The time that the pizza was delivered to the customer
	 * @param type -  A human understandable description of this Pizza type
	 * @param price - The price that the pizza is sold to the customer
	 * @throws PizzaException if supplied parameters are invalid 
	 * 
	 */
	private String type;
	private int quantity;
	private double price;
	private double costPerPizza;
	private PizzaTopping toppingsList[];
	public Pizza(int quantity, LocalTime orderTime, LocalTime deliveryTime, String type, double price) throws PizzaException{
		//Check quantity is between 1 and 10
		if ( quantity < 1 || quantity > 10){
			throw new PizzaException("Quantity: " + quantity + " is not valid.");
		}
		//Check if order was made during open hours
		if (orderTime.isAfter(LocalTime.of(23, 00, 00))  || orderTime.isBefore(LocalTime.of(7, 00, 00))){
			throw new PizzaException("Time: " + orderTime + " is outside open hours.");
		}
		//Check for delivery times before order times
		if (deliveryTime.isBefore(orderTime)){
			throw new PizzaException("Can not delivery before order placed.");
		}
		//Check whether the delivery was made before the order was ready
		if (deliveryTime.isBefore(orderTime.plusMinutes(10))){
			throw new PizzaException("Order not ready in time for delivery.");
		}
		//Check if the pizza has not been delivered within the hour, and so thrown out
		if (deliveryTime.isAfter(orderTime.plusMinutes(10).plusHours(1))){
			throw new PizzaException("Pizza thrown out.");
		}
		//Setup topping depending on the type of pizza
		if(type.equals("Vegetarian")){
			toppingsList = new PizzaTopping[]{PizzaTopping.TOMATO, PizzaTopping.CHEESE, PizzaTopping.EGGPLANT, PizzaTopping.MUSHROOM, PizzaTopping.CAPSICUM};
		} else if (type.equals("Meat Lovers")){
			toppingsList = new PizzaTopping[]{PizzaTopping.TOMATO, PizzaTopping.CHEESE};
		} else if(type.equals("Margherita")){
			toppingsList = new PizzaTopping[]{PizzaTopping.TOMATO, PizzaTopping.CHEESE, PizzaTopping.BACON, PizzaTopping.PEPPERONI,PizzaTopping.SALAMI};
		}
		
		this.type = type;
		this.quantity = quantity;
		this.price = price;
		calculateCostPerPizza();
	}

	/**
	 * Calculates how much a pizza would could to make calculated from its toppings.
	 *  
     * <P> PRE: TRUE
	 * <P> POST: The cost field is set to sum of the Pizzas's toppings
	 */
	public final void calculateCostPerPizza(){
		costPerPizza = 0;
		for(PizzaTopping toppingCheck: toppingsList){
			if(containsTopping(toppingCheck)){
				costPerPizza += toppingCheck.getCost();
			}
		}
	}
	
	/**
	 * Returns the amount that an individual pizza costs to make.
	 * @return The amount that an individual pizza costs to make.
	 */
	public final double getCostPerPizza(){
		return costPerPizza;
	}

	/**
	 * Returns the amount that an individual pizza is sold to the customer.
	 * @return The amount that an individual pizza is sold to the customer.
	 */
	public final double getPricePerPizza(){
		return price;
	}

	/**
	 * Returns the amount that the entire order costs to make, taking into account the type and quantity of pizzas. 
	 * @return The amount that the entire order costs to make, taking into account the type and quantity of pizzas. 
	 */
	public final double getOrderCost(){
		return costPerPizza*quantity;
	}
	
	/**
	 * Returns the amount that the entire order is sold to the customer, taking into account the type and quantity of pizzas. 
	 * @return The amount that the entire order is sold to the customer, taking into account the type and quantity of pizzas. 
	 */
	public final double getOrderPrice(){
		return price*quantity;
	}
	
	
	/**
	 * Returns the profit made by the restaurant on the order which is the order price minus the order cost. 
	 * @return  Returns the profit made by the restaurant on the order which is the order price minus the order cost.
	 */
	public final double getOrderProfit(){
		return (price - costPerPizza)*quantity;
	}
	

	/**
	 * Indicates if the pizza contains the specified pizza topping or not. 
	 * @param topping -  A topping as specified in the enumeration PizzaTopping
	 * @return Returns  true if the instance of Pizza contains the specified topping and false otherwise.
	 */
	public final boolean containsTopping(PizzaTopping topping){
		for(PizzaTopping toppingCheck: toppingsList){
			if(toppingCheck.equals(topping))
				return true;
		}
		return false;
	}
	
	/**
	 * Returns the quantity of pizzas ordered. 
	 * @return the quantity of pizzas ordered. 
	 */
	public final int getQuantity(){
		return quantity;
	}

	/**
	 * Returns a human understandable description of the Pizza's type. 
	 * The valid alternatives are listed in Section 5.1 of the Assignment Specification. 
	 * @return A human understandable description of the Pizza's type.
	 */
	public final String getPizzaType(){
		return type;
	}


	/**
	 * Compares *this* Pizza object with an instance of an *other* Pizza object and returns true if  
	 * if the two objects are equivalent, that is, if the values exposed by public methods are equal.
	 * You do not need to test this method.
	 *  
	 * @return true if *this* Pizza object and the *other* Pizza object have the same values returned for 	
	 * getCostPerPizza(), getOrderCost(), getOrderPrice(), getOrderProfit(), getPizzaType(), getPricePerPizza() 
	 * and getQuantity().
	 *   
	 */
	@Override
	public boolean equals(Object other){
		Pizza otherPizza = (Pizza) other;
		return ((this.getCostPerPizza()) == (otherPizza.getCostPerPizza()) &&
			(this.getOrderCost()) == (otherPizza.getOrderCost())) &&				
			(this.getOrderPrice()) == (otherPizza.getOrderPrice()) &&
			(this.getOrderProfit()) == (otherPizza.getOrderProfit()) &&
			(this.getPizzaType() == (otherPizza.getPizzaType()) &&
			(this.getPricePerPizza()) == (otherPizza.getPricePerPizza()) &&
			(this.getQuantity()) == (otherPizza.getQuantity()));
	}

	
}

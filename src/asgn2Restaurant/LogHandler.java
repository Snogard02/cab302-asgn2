package asgn2Restaurant;


import java.util.ArrayList;
import asgn2Customers.Customer;
import asgn2Customers.CustomerFactory;
import asgn2Exceptions.CustomerException;
import asgn2Exceptions.LogHandlerException;
import asgn2Exceptions.PizzaException;
import asgn2Pizzas.Pizza;
import asgn2Pizzas.PizzaFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 *
 * A class that contains methods that use the information in the log file to return Pizza 
 * and Customer object - either as an individual Pizza/Customer object or as an
 * ArrayList of Pizza/Customer objects.
 * 
 * @author Joshua Serong and Christopher Dare
 *
 */
public class LogHandler {
	public static final int DATALENGTH = 9;
	public static final String SEPERATOR = ",";
	/**
	 * Returns an ArrayList of Customer objects from the information contained in the log file ordered as they appear in the log file.
	 * @param filename The file name of the log file
	 * @return an ArrayList of Customer objects from the information contained in the log file ordered as they appear in the log file. 
	 * @throws CustomerException If the log file contains semantic errors leading that violate the customer constraints listed in Section 5.3 of the Assignment Specification or contain an invalid customer code (passed by another class).
	 * @throws LogHandlerException If there was a problem with the log file not related to the semantic errors above
	 * 
	 * Author: Christopher Dare
	 */
	public static ArrayList<Customer> populateCustomerDataset(String filename) throws CustomerException, LogHandlerException{
		ArrayList<Customer> customerArray = new ArrayList<Customer>();
		BufferedReader reader;
		String line;
		
		try{
			reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null){
				customerArray.add(createCustomer(line));
			}
			reader.close();
		}catch (IOException e){
			throw new LogHandlerException("Can not read: " + filename);
		}
		return customerArray;
	}		

	/**
	 * Returns an ArrayList of Pizza objects from the information contained in the log file ordered as they appear in the log file. .
	 * @param filename The file name of the log file
	 * @return an ArrayList of Pizza objects from the information contained in the log file ordered as they appear in the log file. .
	 * @throws PizzaException If the log file contains semantic errors leading that violate the pizza constraints listed in Section 5.3 of the Assignment Specification or contain an invalid pizza code (passed by another class).
	 * @throws LogHandlerException If there was a problem with the log file not related to the semantic errors above
	 * 
	 */
	public static ArrayList<Pizza> populatePizzaDataset(String filename) throws PizzaException, LogHandlerException{
		ArrayList<Pizza> pizzaArray= new ArrayList<Pizza>();
		BufferedReader reader;
		String line;
		try{
			reader = new BufferedReader(new FileReader(filename));
			while((line = reader.readLine()) != null){
				pizzaArray.add(createPizza(line));
			}
			reader.close();
		} catch (IOException e){
			throw new LogHandlerException("Can not read: " + filename);
		}
		return pizzaArray;	
	}		

	
	/**
	 * Creates a Customer object by parsing the  information contained in a single line of the log file. The format of 
	 * each line is outlined in Section 5.3 of the Assignment Specification.  
	 * @param line - A line from the log file
	 * @return- A Customer object containing the information from the line in the log file
	 * @throws CustomerException - If the log file contains semantic errors leading that violate the customer constraints listed in Section 5.3 of the Assignment Specification or contain an invalid customer code (passed by another class).
	 * @throws LogHandlerException - If there was a problem parsing the line from the log file.
	 * 
	 * Author: Christopher Dare
	 */
	public static Customer createCustomer(String line) throws CustomerException, LogHandlerException{
		String[] data;
		data = line.split(SEPERATOR);
		if(data.length != DATALENGTH){
			throw new LogHandlerException("Input data is wrong dimensions");
		}
		try{
			return  CustomerFactory.getCustomer(data[4], data[2], data[3], Integer.parseInt(data[5]), Integer.parseInt(data[6]));
		} catch (NumberFormatException e){
			throw new LogHandlerException("Wrong data format in locationX or locationY");
		} catch (CustomerException e){
			throw new CustomerException("Data parsed does not conform to document standards", e);
		}
	}
	
	/**
	 * Creates a Pizza object by parsing the information contained in a single line of the log file. The format of 
	 * each line is outlined in Section 5.3 of the Assignment Specification.  
	 * @param line - A line from the log file
	 * @return- A Pizza object containing the information from the line in the log file
	 * @throws PizzaException If the log file contains semantic errors leading that violate the pizza constraints listed in Section 5.3 of the Assignment Specification or contain an invalid pizza code (passed by another class).
	 * @throws LogHandlerException - If there was a problem parsing the line from the log file.
	 */
	public static Pizza createPizza(String line) throws PizzaException, LogHandlerException{
		String[] data;
		data = line.split(SEPERATOR);
		if(data.length != DATALENGTH){
			throw new LogHandlerException("Input data is wrong dimensions");
		}
		try{
			return PizzaFactory.getPizza(data[7], Integer.parseInt(data[8]), LocalTime.parse(data[0]), LocalTime.parse(data[1]));
		} catch (NumberFormatException | DateTimeParseException e){
			throw new LogHandlerException("Wrong data format");
		}
	}
}

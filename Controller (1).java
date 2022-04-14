// The Stock program is following the MVC design template and this is our controller object.
// The main functionality for buying and selling the stocks are in this controller object.
// This is the ONLY file you may edit

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Controller {

	ArrayList<LinkedList<Stock>> stockList = new ArrayList<>();

	public void buyStock(Stock stock)
	{
		for (LinkedList<Stock> stockType : stockList) 
		{
			if(stockType.getLast().getName().equals(stock.getName()))
			{
				stockType.push(stock);
				return;
			}
		}
		LinkedList<Stock> newStockType = new LinkedList<Stock>();
		newStockType.push(stock);
		stockList.add(newStockType);
	}

	public double sellStock(String accountingStyle, Stock stock)
	{
		double profit = 0;
		switch(accountingStyle)
		{
			case "l":
			for (LinkedList<Stock> linkedList : stockList) 
			{
				if(linkedList.element().getName().equals(stock.getName()))
				{
					while(stock.getQuantity() > 0 && linkedList.size() > 0)
					{
						Stock soldStock = linkedList.removeFirst();
						double priceDifferencePerShare = stock.getPrice() - soldStock.getPrice();
						int sharesSold = Math.min(stock.getQuantity(), soldStock.getQuantity());
						stock.setQuantity(stock.getQuantity() - sharesSold);
						soldStock.setQuantity(soldStock.getQuantity() - sharesSold);
						if(soldStock.getQuantity() > 0)
						{
							linkedList.addFirst(soldStock);
						}
						profit += priceDifferencePerShare * sharesSold;
					}
				}
			}
			return profit;
			case "f":
				for (LinkedList<Stock> linkedList : stockList) 
				{
					if(linkedList.element().getName().equals(stock.getName()))
					{
						while(stock.getQuantity() > 0 && linkedList.size() > 0)
						{
							Stock soldStock = linkedList.removeLast();
							double priceDifferencePerShare = stock.getPrice() - soldStock.getPrice();
							int sharesSold = Math.min(stock.getQuantity(), soldStock.getQuantity());
							stock.setQuantity(stock.getQuantity() - sharesSold);
							soldStock.setQuantity(soldStock.getQuantity() - sharesSold);
							if(soldStock.getQuantity() > 0)
							{
								linkedList.add(soldStock);
							}
							profit += priceDifferencePerShare * sharesSold;
						}
					}
				}
				return profit;	
			default:
				break;
		}

		return profit;
	}

	public boolean canSellStock(Stock stock)
	{
		int availableShares = 0;
		for (LinkedList<Stock> linkedList : stockList) 
		{
			if(linkedList.element().getName().equals(stock.getName()))
			{
				for (Stock stockFromList : linkedList) 
				{
					availableShares += stockFromList.getQuantity();
					if(availableShares >= stock.getQuantity())
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public void printCurrentHoldings()
	{
		System.out.println("Current Holdings");
		for (LinkedList<Stock> linkedList : stockList) 
		{
			int stockTotal = 0;
			String tickerSymbol = linkedList.getLast().getName();

			for (Stock stock : linkedList)
			{
				stockTotal += stock.getQuantity();
			}

			System.out.printf("%s : %d \n", tickerSymbol, stockTotal);
		}
	}

	public Controller() {

		stockList = new ArrayList<>();
		Scanner input = new Scanner(System.in);
		
		do {
			System.out.println("\nAccount Main Menu");
			System.out.println("Buy a stock (b)");
			System.out.println("Sell a stock (s)");
			System.out.println("View Current Holdings (v)");
			System.out.println("Quit program (q)");
			
			String userInput = input.next().toLowerCase();
			String stockName;
			int quantity;
			double pricePerShare;
			switch(userInput)
			{
				case "b":
					System.out.print("Which stock would you like to invest in?");
					stockName = input.next();
					System.out.print("Share Number?");
					quantity = input.nextInt();
					System.out.print("Price Per Share?");
					pricePerShare = (double)input.nextInt();
					buyStock(new Stock(stockName,quantity,pricePerShare));
					System.out.printf("Successfully Bought " + quantity + " shares of " + stockName + " at $%.2f per share ", pricePerShare );
					break;
				case "s":
					System.out.println("Sell With Accounting Type");
					System.out.println("LIFO (L)");
					System.out.println("FIFO (F)");
					String accountingStyle = input.next().toLowerCase().substring(0, 1);
					if(!accountingStyle.startsWith("l") && !accountingStyle.startsWith("f"))
					{
						System.out.println("Invalid User Input of: '" + userInput + "' \nReturning to Main Menu");
						break;
					}
					System.out.print("Which stock would you like to sell?");
					stockName = input.next();
					System.out.print("Share Number?");
					quantity = input.nextInt();
					System.out.print("Price Per Share?");
					pricePerShare = input.nextInt();
					Stock stockToSell = new Stock(stockName,quantity,pricePerShare);
					boolean validTransaction = canSellStock(stockToSell);
					
					if(validTransaction)
					{
						double profit = sellStock(accountingStyle, stockToSell);
						System.out.printf("Successfully Sold " + quantity + " shares of " + stockName + " at $%.2f per share ", pricePerShare );
						System.out.printf("\nYou made $%.2f on the sale!", profit);
					}
					else
					{
						System.out.printf("Transaction Failed. Unable to sell %d shares of %s\n", quantity, stockName);	
						System.out.println("Please View Current Holdings.");
					}
					break;
				case "q":
					input.close();
					return;
				case "v":
					printCurrentHoldings();
					break;
				default:
					System.out.println("Invalid User Input of: '" + userInput + "' \nReturning to Main Menu");
			}
		} while(true);
	}
}

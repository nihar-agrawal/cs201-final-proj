package com.cv.jdbc.get;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cv.jdbc.ConnectToDB;

public class GetCurrencyInformation {
	
	
	
	public GetCurrencyInformation(){
		
	}
	
	public Vector<CurrencyID_Ticker> getAllTickers(){
		Connection conn = ConnectToDB.getDBConnection();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<CurrencyID_Ticker> allTickersVector = new Vector<CurrencyID_Ticker>();
	
		try {
			
			
			String Statement = "SELECT * FROM currencyID";
			
			ps = conn.prepareStatement(Statement);
			rs = ps.executeQuery();
			
			
			
			while(rs.next()) {
				int ID = rs.getInt(1);
				String ticker = rs.getString(2);
				CurrencyID_Ticker temp = new CurrencyID_Ticker(ID, ticker);
				allTickersVector.add(temp);
			}
			
		} catch(Exception e){
			System.out.println("Exception Thrown");
			return allTickersVector;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
			
		}
		return allTickersVector;
	}
	
	public boolean isValidCurrencyID(int currencyID) {
		Connection conn = ConnectToDB.getDBConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean validCurrencyID = false;
		
		try {
			
			String validationStatement = "SELECT * FROM CurrencyInfo WHERE currencyID=?";
			
			ps = conn.prepareStatement(validationStatement);
			ps.setInt(1, currencyID);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("currencyID");

				if(id == currencyID) {
					validCurrencyID = true;
					return validCurrencyID;
				}
			}
			
		} catch(Exception e){
			return validCurrencyID;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
			
		}
		return validCurrencyID;
	}
	
	public String getTickerFromID(int IDToLookFor) {
		Connection conn = ConnectToDB.getDBConnection();
		String ticker = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
			String Statement = "SELECT * FROM CurrencyInfo WHERE currencyID=?";
			
			ps = conn.prepareStatement(Statement);
			ps.setInt(1, IDToLookFor);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				ticker = rs.getString("ticker");
				int thisID = rs.getInt("currencyID");
				
				if(IDToLookFor == thisID) {
					return ticker;
				}
			}
			
		} catch(Exception e){
			System.out.println("Exception Thrown");
			return ticker;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
			
		}
		return ticker;
	}
	
	public int getIDFromTicker(String tickerToLookFor){
		
	
		Connection conn = ConnectToDB.getDBConnection();
		int currencyID = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
	
		try {
			String Statement = "SELECT * FROM CurrencyInfo WHERE ticker=?";
			
			ps = conn.prepareStatement(Statement);
			ps.setString(1, tickerToLookFor);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				currencyID = rs.getInt("currencyID");
				String thisTicker = rs.getString("ticker");
				
				if(tickerToLookFor.equals(thisTicker)) {
					return currencyID;
				}
			}
			
		} catch(Exception e){
			System.out.println("Exception Thrown");
			return currencyID;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
			
		}
		return currencyID;
	}
	
}

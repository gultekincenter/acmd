package com.acms.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.acms.jdbc.GetProperty;
import com.acms.jdbc.Property;

public class PropertyDbUtil {

	SqliteConUtil conn = new SqliteConUtil();

	public PropertyDbUtil(SqliteConUtil con) {
		con = this.conn;
	}

	public List<GetProperty> getProperties(String search) throws Exception {

		List<GetProperty> properties = new ArrayList<>();
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		String sql = null;

		try {
			// get a connection
			myConn = conn.getMySQLConnection();

			System.out.println("Your here now : " + search);

/*			if (!search.isEmpty()) {

				// create sql statement
				sql = "SELECT tbl_property.property_id AS property_id, " + 
						"tbl_property_types.type_name AS type_name, " + 
						"tbl_property.address AS address, tbl_property.suitable_for AS suitable_for, " + 
						"CASE (tbl_property.is_available) " + 
						"WHEN '1' THEN 'available' ELSE 'Occupied' END AS availability, " + 
						"tbl_owner.first_name AS owner, " + 
						"CASE tbl_property.rented_by " + 
						"WHEN '0' THEN 'none' ELSE tbl_student.first_name END as rented_by, " + 
						"tbl_property.charge AS rent " + 
						"FROM tbl_property INNER JOIN tbl_property_types ON " + 
						"tbl_property_types.type_id = tbl_property.property_type " + 
						"INNER JOIN tbl_owner ON  tbl_property.owner = tbl_owner.owner_id " + 
						"LEFT JOIN tbl_student ON  tbl_property.rented_by = tbl_student.student_id " + 
						"WHERE tbl_property.isDeleted = 1 AND type_name LIKE ? OR address LIKE ?";

				myStmt = myConn.prepareStatement(sql);
				// prepare statement

				// set param
				myStmt.setString(1, "%" + search + "%");
				myStmt.setString(2, "%" + search + "%");*/
		//	} else {
				// create sql statement
				sql = "SELECT tbl_property.property_id AS property_id, " + 
						"tbl_property_types.type_name AS type_name, " + 
						"tbl_property.address AS address, tbl_property.suitable_for AS suitable_for, " + 
						"CASE (tbl_property.is_available) " + 
						"WHEN '1' THEN 'available' ELSE 'Occupied' END AS availability, " + 
						"tbl_owner.first_name AS owner, " + 
						"CASE tbl_property.rented_by " + 
						"WHEN '0' THEN 'none' ELSE tbl_student.first_name END as rented_by, " + 
						"tbl_property.charge AS rent " + 
						"FROM tbl_property INNER JOIN tbl_property_types ON " + 
						"tbl_property_types.type_id = tbl_property.property_type " + 
						"INNER JOIN tbl_owner ON  tbl_property.owner = tbl_owner.owner_id " + 
						"LEFT JOIN tbl_student ON  tbl_property.rented_by = tbl_student.student_id " + 
						"WHERE tbl_property.isDeleted = 1";

				myStmt = myConn.prepareStatement(sql);
			//}

			System.out.println(search);

			// execute query
			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {
				// retrieve data from result set row
				int property_id = myRs.getInt("property_id");
				String type_name = myRs.getString("type_name");
				String address = myRs.getString("address");
				int suitable_for = myRs.getInt("suitable_for");
				String availability = myRs.getString("availability");
				String owner = myRs.getString("owner");
				String rented_by = myRs.getString("rented_by");
				float rent = myRs.getFloat("rent");

				// create new property object
				GetProperty tempProperty = new GetProperty(property_id, type_name, address, suitable_for, availability,
						owner, rented_by, rent);

				// add it to the list of properties
				properties.add(tempProperty);
			}
			return properties;

		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void addProperty(Property theProperty) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = conn.getMySQLConnection();

			// create sql for insert
			String sql = "insert into tbl_property "
					+ "(property_type, suitable_for, is_available, owner, rented_by, charge) "
					+ "values (?, ?, ?, ?, ?, ?)";

			myStmt = myConn.prepareStatement(sql);

			// set the param values for the owner
			myStmt.setInt(1, theProperty.getProperty_type());
			myStmt.setInt(2, theProperty.getSuitable_for());
			myStmt.setInt(3, theProperty.getIs_available());
			myStmt.setInt(4, theProperty.getOwner());
			myStmt.setInt(5, theProperty.getRented_by());
			myStmt.setFloat(6, theProperty.getCharge());

			// execute sql insert
			myStmt.execute();
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public void updateProperty(Property theProperty) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = conn.getMySQLConnection();

			// create SQL update statement
			String sql = "update tbl_property "
					+ "set property_type=?, suitable_for=?, is_available=? ,owner=? , rented_by=?, charge=? "
					+ "where property_id=?";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, theProperty.getProperty_type());
			myStmt.setInt(2, theProperty.getSuitable_for());
			myStmt.setInt(3, theProperty.getIs_available());
			myStmt.setInt(4, theProperty.getOwner());
			myStmt.setInt(5, theProperty.getRented_by());
			myStmt.setFloat(6, theProperty.getCharge());
			myStmt.setInt(7, theProperty.getProperty_id());

			// execute SQL statement
			myStmt.execute();
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
	}

	public Property getProperty(int property_id) throws Exception {

		Property theProperty = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		try {
			// get connection to database
			myConn = conn.getMySQLConnection();

			// create sql to get selected owner
			String sql = "select * from tbl_property where property_id=?";

			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, property_id);

			// execute statement
			myRs = myStmt.executeQuery();

			// retrieve data from result set row
			while (myRs.next()) {
				int property_type = myRs.getInt("property_type");
				String address = myRs.getString("address");
				int suitable_for = myRs.getInt("suitable_for");
				int is_available = myRs.getInt("is_available");
				int owner = myRs.getInt("owner");
				int rented_by = myRs.getInt("rented_by");
				float charge = myRs.getFloat("charge");
				boolean isDeleted = myRs.getBoolean("isDeleted");

				// use the property during construction
				theProperty = new Property(property_id, property_type, address, suitable_for, is_available, owner,
						rented_by, charge, isDeleted);
			} 

			return theProperty;
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public List<Property> Properties(String search) throws Exception {

		List<Property> properties = new ArrayList<>();
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		String sql = null;

		try {
			// get connection to database
			myConn = conn.getMySQLConnection();

			System.out.println("Your here now : " + search);

			// create sql
			if (!search.isEmpty()) {
				sql = "SELECT * FROM tbl_property WHERE property_type LIKE ? OR address LIKE ?";
				// create prepared statement
				myStmt = myConn.prepareStatement(sql);

				// set params
				myStmt.setString(1, "%" + search + "%");
				myStmt.setString(2, "%" + search + "%");

			} else {
				sql = "SELECT * FROM tbl_property";
				myStmt = myConn.prepareStatement(sql);
			}

			// execute statement
			myRs = myStmt.executeQuery();

			// retrieve data from result set row
			while (myRs.next()) {
				int property_type = myRs.getInt("property_type");
				String address = myRs.getString("address");
				int suitable_for = myRs.getInt("suitable_for");
				int is_available = myRs.getInt("is_available");
				int owner = myRs.getInt("owner");
				int rented_by = myRs.getInt("rented_by");
				float charge = myRs.getFloat("charge");

				// use the property during construction
				Property theProperty = new Property(property_type, address, suitable_for, is_available, owner,
						rented_by, charge);
				properties.add(theProperty);
			} 
			return properties;
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void deleteProperty(int property_id) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get connection to database
			myConn = conn.getMySQLConnection();

			// create sql to delete Property
			String sql = "update `tbl_property` set `isDeleted` = 0 where `property_id` = ?";

			// prepare statement
			myStmt = myConn.prepareStatement(sql);

			// set params
			myStmt.setInt(1, property_id);

			// execute sql statement
			myStmt.execute();
		} finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}
	}

	// method to close and open connection pool
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
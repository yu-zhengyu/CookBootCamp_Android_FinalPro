package dblayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class includes all action about delete record into database
 * @author zhengyu
 *
 */

public class DeleteRecord {
	
	public String deleteMyRecipe(int userid, int recipeid, Connection connection) {

		boolean isyourcollection = false;
		String selectsql = "SELECT * FROM COOKBOOTCAMP.COLLECTION where "
				+ "recipeid=" + recipeid + ";";
		String deletesql = "DELETE FROM COOKBOOTCAMP.COLLECTION where"
				+ " userid=? and recipeid=?;";
		ResultSet result = null;
		Statement statement = null;
		int realid = -1;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(selectsql);
			while (result.next()) {
				realid = result.getInt("userid");
				if(realid == userid) {
					isyourcollection = true;
					break;
				}
			}
			if (!isyourcollection)
				return "This recipe ID is NOT your collection";
			
			PreparedStatement preparedStatement = connection.prepareStatement(deletesql);
			preparedStatement.setInt(1, userid);
			preparedStatement.setInt(2, recipeid);
			
			preparedStatement.executeUpdate();
			preparedStatement.close();
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "Unkonw Error";
		}
		
		return "Success";
	} 

}

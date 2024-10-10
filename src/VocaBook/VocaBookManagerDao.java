package VocaBook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class VocaBookManagerDao {

	private static VocaBookManagerDao instance;

	private VocaBookManagerDao() {
	};

	public static VocaBookManagerDao getInstance() {
		if (instance == null) {
			instance = new VocaBookManagerDao();
		}
		return instance;
	}

	private final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private final String ID = "USER02";
	private final String PW = "1234";
	//private VocaInfoVo info = new VocaInfoVo();

	private Connection getConnection() {

		try {
			Class.forName(DRIVER_NAME);
			Connection conn = DriverManager.getConnection(URL, ID, PW);
			return conn;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		try {
			if (pstmt != null) {
				conn.close();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		try {
			if (rs != null) {
				conn.close();
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	
	public void deleteWord(String word) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			String sql = "delete from tbl_voca"
					+ " where other_word=?";
			conn=this.getConnection();		
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, word);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			closeAll(conn, pstmt, null);
		}
		
	}

	
	public void modifyWord(String o_word,String k_word,String targetWord) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			String sql = "update tbl_voca"
					+ " set other_word=?, ko_word=?"
					+ " where other_word=?";
			conn=this.getConnection();		
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, o_word);
			pstmt.setString(2, k_word);
			pstmt.setString(3, targetWord);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			closeAll(conn, pstmt, null);
		}
		
	}
	
	public void inputData(VocaInfoVo info) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "insert into tbl_voca" + " values(seq_up.nextval,?,?)";
			conn = this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getOtherWord());
			pstmt.setString(2, info.getKoWord());
			pstmt.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeAll(conn, pstmt, null);
		}

	}
	
	
	public String compareWord(String data) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select other_word from"
					+ " tbl_voca where ko_word=?";
			conn = this.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, data);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
			String str=	rs.getString("other_word");
			return str;
			}
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			closeAll(conn, pstmt, rs);
		}

		return null;
	}
	
	

	public Vector<VocaInfoVo> showWord() {
		Connection conn = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		
		
		try {
			String sql = "select other_word, ko_word"
					+ " from tbl_voca order by word_no";
			conn=this.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs= pstmt.executeQuery();
		
			Vector<VocaInfoVo> data = new Vector<VocaInfoVo>();
			while(rs.next()) {
				String otherWord = rs.getString("other_word");
				String koWord = rs.getString("ko_word");
				VocaInfoVo info = new VocaInfoVo(otherWord, koWord);
				data.add(info);
				//otherData.add(otherWord);
				
			}
			return data;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

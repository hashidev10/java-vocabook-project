package VocaBook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class VocaBookFrame extends JFrame {

	private JTextField tfSearch = new JTextField(10);
	private JTextField tfInput = new JTextField(10);
	private JTextArea taRecentSearch = new JTextArea();
	private JTextArea taFavoriteWord = new JTextArea();
	private JButton btnAddFavor = new JButton("추가");
	private JButton btnDeleteWord = new JButton("삭제");
	private JButton btnDeleteFavor = new JButton("삭제");
	private JButton btnSearch = new JButton("찾기");
	private JButton btnInputWord = new JButton("추가");
	private JButton btnModWord = new JButton("수정");
	private JButton btnStartGame = new JButton("연습게임");
	private Font defaultFont = new Font("맑은 고딕", Font.PLAIN, 20);
	private ActionListener listener = new ActListener();
	private menuActionListener menuListener = new menuActionListener();
	private WordInputDialog inpDialog;
	private WordModifyDialog modDialog=new WordModifyDialog(false, null);
	private VocaBookManagerDao manager = VocaBookManagerDao.getInstance();
	private VocaInfoVo info;
	private Container c = getContentPane();
	private JTable tblMainView;
	private JTable tblFavorView;
	private Vector<String> mainTitleCol = new Vector<String>();
	private Vector<String> favorTitleCol = new Vector<String>();
	private DefaultTableModel mainModel = new DefaultTableModel(mainTitleCol, 0);
	private DefaultTableModel favorModel = new DefaultTableModel(favorTitleCol, 0);
	private String[] viewOptionMenu = new String[] { "단어만", "뜻만", "모두보기" };
	private JComboBox cbWordViewOption = new JComboBox(viewOptionMenu);
	private PracticeGameRunDialog dialogGameRun;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu mnuFile = new JMenu("파일");
	private JMenu mnuOption = new JMenu("옵션");
	private JMenu mnuHelp = new JMenu("도움말");
	private JMenuItem miSave = new JMenuItem("저장");
	private JMenuItem miLoad = new JMenuItem("불러오기");
	private JMenuItem miUserOption = new JMenuItem("환경설정");
	private JMenuItem miHelpView = new JMenuItem("도움말 보기");
	private OptionViewDialog optionDialog;
	private JFileChooser chooser = new JFileChooser();
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("저장 파일(.sav)", "sav");

	public VocaBookFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("단어장");
		setSize(560, 460);

//		setResizable(false);
		setInit();
		setUI();

		setListener();
		tblMainView.setFont(new Font("굴림", Font.PLAIN, 12));
		setVisible(true);

	}

	private void saveFile() {
		int choose = chooser.showSaveDialog(this);

		if (choose == JFileChooser.APPROVE_OPTION) {

			File f = chooser.getSelectedFile();
			// String fileName = f.getName();
			String filePath = f.getPath();
			fWriter(filePath);

		}

	}

	private void fWriter(String path) {
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(taFavoriteWord.getText());
			JOptionPane.showMessageDialog(c, "파일저장완료");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadFile() {
		int choose = chooser.showOpenDialog(this);

		if (choose == JFileChooser.APPROVE_OPTION) {

			File f = chooser.getSelectedFile();
			// String fileName = f.getName();
			String filePath = f.getPath();
			fReader(filePath);

		}
	}

	private void fReader(String path) {
		FileReader fr = null;
		BufferedReader br = null;
		taFavoriteWord.setText("");
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			while (true) {
				String line = br.readLine(); // 엔터키 전까지
				if (line == null) {
					break;
				}

				taFavoriteWord.append(line + "\n");
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class OptionViewDialog extends JDialog implements ActionListener {

		Integer[] fontSize = new Integer[] { 8, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30 };

		String[] fontDepth = new String[] { "보통", "굵게", "기울임" };

		String[] fontShape = new String[] { "굴림", "바탕", "돋움", "궁서" };
		JComboBox cbFontSizeSelect = new JComboBox(fontSize);
		JComboBox cbDeptSelect = new JComboBox(fontDepth);
		JComboBox cbFontShape = new JComboBox(fontShape);
		Container optionC = this.getContentPane();

		JButton btnResetOption = new JButton("초기값 설정");
		JButton btnOptionClose = new JButton("설정종료");

		Integer fontDeptType = Font.PLAIN;

		OptionViewDialog() {
			setTitle("환경설정");
			cbFontSizeSelect.setSelectedIndex(3);

			setSize(400, 150);
			setOptionMenuUI();
			setOptionMenuListener();
			setVisible(true);
		}

		private void setOptionMenuListener() {
			cbFontSizeSelect.addActionListener(this);
			cbDeptSelect.addActionListener(this);
			cbFontShape.addActionListener(this);
			btnOptionClose.addActionListener(this);
			btnResetOption.addActionListener(this);

		}

		private void setOptionMenuUI() {
			JPanel panel = new JPanel();
			JLabel lblFontSize = new JLabel("글꼴 크기:");
			JLabel lblLangShape = new JLabel("글꼴:");
			JLabel lblLangDepth = new JLabel("글꼴 스타일:");
			panel.setLayout(new FlowLayout());
			panel.add(lblFontSize);
			panel.add(cbFontSizeSelect);
			panel.add(lblLangDepth);
			panel.add(cbDeptSelect);
			panel.add(lblLangShape);
			panel.add(cbFontShape);

			panel.add(btnResetOption);
			panel.add(btnOptionClose);

			optionC.add(panel);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			Object obj = e.getSource();
			Integer fontSize = (Integer) cbFontSizeSelect.getSelectedItem();
			String fontShape = (String) cbFontShape.getSelectedItem();
			if (obj == cbFontSizeSelect) {

				tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
			} else if (obj == cbFontShape) {

				tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
			} else if (obj == cbDeptSelect) {

				switch (cbDeptSelect.getSelectedIndex()) {
				case 0:
					fontDeptType = Font.PLAIN;
					tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
					break;
				case 1:
					fontDeptType = Font.BOLD;
					tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
					break;
				case 2:
					fontDeptType = Font.ITALIC;
					tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
					break;

				}
			} else if (obj == btnResetOption) {
				fontDeptType = Font.PLAIN;
				fontSize = 12;
				fontShape = "굴림";
				cbFontSizeSelect.setSelectedIndex(3);
				cbDeptSelect.setSelectedIndex(0);
				cbFontShape.setSelectedIndex(0);
				tblMainView.setFont(new Font(fontShape, fontDeptType, fontSize));
			}

			else if (obj == btnOptionClose) {
				this.setVisible(false);
			}

		}

	}

	// 메뉴관련 액션리스너
	private class menuActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			Object obj = e.getSource();

			if (obj == miHelpView) {
				JOptionPane.showMessageDialog(c, "추가 : 즐겨찾기에 단어 추가\n"
						+ "수정 : 선택된 테이블의 단어를 수정\n" 
			+ "삭제 : 단어 항목 삭제\n" + "단어추가 : 단어 항목 추가\n"
					+ "연습게임 : 저장된 단어가 5개이상일때 연습게임 시작가능");

			} else if (obj == miUserOption) {
				optionDialog = new OptionViewDialog();

			} else if (obj == miSave) {
				saveFile();

			} else if (obj == miLoad) {
				loadFile();
			}

		}

	}

	private void setListener() {
		tfSearch.addActionListener(listener);
		btnSearch.addActionListener(listener);
		btnInputWord.addActionListener(listener);
		btnAddFavor.addActionListener(listener);
		btnDeleteWord.addActionListener(listener);
		btnDeleteFavor.addActionListener(listener);
		btnModWord.addActionListener(listener);
		cbWordViewOption.addActionListener(listener);
		btnStartGame.addActionListener(listener);
		miHelpView.addActionListener(menuListener);
		miUserOption.addActionListener(menuListener);
		miSave.addActionListener(menuListener);
		miLoad.addActionListener(menuListener);

	}

	private void setUI() {
		setMenu();
		setNorth();
		setCenter();

	}

	private void setInit() {
		taFavoriteWord.setEditable(false);
		taRecentSearch.setEditable(false);
		inpDialog = new WordInputDialog(false);
		
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File("./"));

	}

	private void setMenu() {

		mnuFile.add(miSave);
		mnuFile.add(miLoad);
		mnuOption.add(miUserOption);
		mnuHelp.add(miHelpView);
		menuBar.add(mnuFile);
		menuBar.add(mnuOption);
		menuBar.add(mnuHelp);

		setJMenuBar(menuBar);

	}

	private void setNorth() {
		JPanel panel = new JPanel();

		panel.setBackground(new Color(213, 229, 244));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(tfSearch);
		panel.add(btnSearch);
		panel.add(btnInputWord);
		panel.add(btnModWord);
		panel.add(btnDeleteWord);
		panel.add(cbWordViewOption);

		panel.add(btnStartGame);

		cbWordViewOption.setSelectedIndex(2); // 2 모두보기
		// System.out.println("cb:" + cbWordViewOption.getSelectedIndex());

		taRecentSearch.setBackground(new Color(213, 229, 244));
		taRecentSearch.setFont(defaultFont);
		panel.add(taRecentSearch);
		c.add(panel, BorderLayout.NORTH);
	}

	private void setCenter() {

		CenterPanel panel = new CenterPanel();
		tblMainView = viewWord();

		panel.setLayout(new BorderLayout());
		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new BorderLayout());
		pnlLeft.add(taFavoriteWord, BorderLayout.CENTER);

		JPanel pnlFavor = new JPanel();
		JLabel lblFavor = new JLabel("즐겨찾기");
		pnlFavor.add(lblFavor);
		pnlLeft.add(pnlFavor, BorderLayout.CENTER);

		JPanel pnlSouth = new JPanel();
		pnlSouth.add(btnAddFavor);
		pnlSouth.add(btnDeleteFavor);
		pnlLeft.add(pnlSouth, BorderLayout.SOUTH);
		panel.add(pnlLeft, BorderLayout.WEST);
		panel.add(new JScrollPane(tblMainView), BorderLayout.CENTER);

		taFavoriteWord.setText("즐겨 찾기");
		taFavoriteWord.setBounds(0, 0, 192, 330);
		btnAddFavor.setBounds(91, 330, 100, 33);
		btnDeleteWord.setBounds(0, 330, 100, 33);
		taFavoriteWord.setBackground(new Color(254, 255, 235));
		taFavoriteWord.setFont(defaultFont);

		c.add(panel, BorderLayout.CENTER);

	}

	private void gameDialogCreate() {

		dialogGameRun = new PracticeGameRunDialog(true);

	}

	private class CenterPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.black);
			g.drawLine(193, 1, 193, 400);
			g.drawLine(193, 1, 600, 1);
		}
	}

	public JTable viewWord() {

		mainTitleCol.add("단어");
		mainTitleCol.add("뜻");

		Vector<VocaInfoVo> data = manager.showWord();

		for (VocaInfoVo info : data) {
			String other = info.getOtherWord();
			String ko = info.getKoWord();

			String[] str = new String[] { other, ko };
			mainModel.addRow(str);

		}

		tblMainView = new JTable(mainModel);
		return tblMainView;

	}

	public void delTable() {
		int row = tblMainView.getSelectedRow();
		int col = tblMainView.getSelectedColumn();
		String selectWord = (String) tblMainView.getValueAt(row, col);
		manager.deleteWord(selectWord);
		mainModel.removeRow(row);

	}
	

	public class WordModifyDialog extends JDialog{
		JLabel lblOtherWord = new JLabel("단어:");
		JTextField tfOtherWord = new JTextField(5);
		JLabel lblKoWord = new JLabel("뜻:");
		JTextField tfKoWord = new JTextField(5);
		JButton btnModifyWord = new JButton("수정");
		String targetWord=null;
	
		public WordModifyDialog(boolean modal,String modWord){
			
			setVisible(modal);
			setSize(300, 100);
			setTitle("단어수정");
			
			targetWord=modWord;
			modTable();
			this.setWIUI(modWord);
		}

		private void setWIUI(String modWord) {

		
			JPanel panel = new JPanel();
			panel.add(lblOtherWord);
			panel.add(tfOtherWord);
			panel.add(lblKoWord);
			panel.add(tfKoWord);
			panel.add(btnModifyWord);
			panel.add(new JLabel("수정단어:"+modWord));
		
			this.add(panel);
			this.setResizable(false);
			
		}
		

		private void modTable() {
			btnModifyWord.addActionListener((e)->{
				manager.modifyWord(tfOtherWord.getText(), tfKoWord.getText(), targetWord);
				changeModTable(tfOtherWord.getText(), tfKoWord.getText());
				
			});
			
		}
		
		private void changeModTable(String word1, String word2) {
			int row = tblMainView.getSelectedRow();
			int col = tblMainView.getSelectedColumn();
			
			mainModel.setValueAt(word1,row,col);
			mainModel.setValueAt(word2,row,col+1);
		}
		
	}
	


	public class WordInputDialog extends JDialog implements ActionListener {
		JLabel lblOtherWord = new JLabel("단어:");
		JTextField tfOtherWord = new JTextField(5);
		JLabel lblKoWord = new JLabel("뜻:");
		JTextField tfKoWord = new JTextField(5);
		JButton btnAddWord = new JButton("추가");

		public WordInputDialog(boolean modal) {
			setSize(300, 100);
			setTitle("단어추가");
			this.setVisible(modal);
			setLocation(200, 250);
			this.setWIUI();
			btnAddWord.addActionListener(this);
		}

		private void setWIUI() {
			JPanel panel = new JPanel();
			panel.add(lblOtherWord);
			panel.add(tfOtherWord);
			panel.add(lblKoWord);
			panel.add(tfKoWord);
			panel.add(btnAddWord);
			this.add(panel);
			this.setResizable(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			VocaInfoVo info;

			String otherWord = tfOtherWord.getText();
			String koWord = tfKoWord.getText();

			if (obj == btnAddWord) {

				if (!otherWord.equals("") && !koWord.equals("")) {
					info = new VocaInfoVo(otherWord, koWord);
					String addWord[] = new String[] { otherWord, koWord };
					manager.inputData(info);
					mainModel.addRow(addWord);
				} else {
					JOptionPane.showMessageDialog(this, "단어를 입력해주세요");
				}

			}

		}

	}

	private void searchWord(String data) {

		for (int i = 0; i < tblMainView.getRowCount(); i++) {
			String col1 = (String) tblMainView.getValueAt(i, 0);
			String col2 = (String) tblMainView.getValueAt(i, 1);
			if (data.equals(col1)) {
				tblMainView.setRowSelectionInterval(i, i);

			} else if (data.equals(col2)) {
				tblMainView.setRowSelectionInterval(i, i);

			}

		}

	}

	public class ActListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();

			if (obj == btnInputWord) {

				if (!inpDialog.isVisible()) {
					inpDialog.setVisible(true);

				}

			} else if(obj == btnModWord) {
				int row = tblMainView.getSelectedRow();
				int col = tblMainView.getSelectedColumn();
				String selectWord = (String) tblMainView.getValueAt(row, col);
				
				if(tblMainView.isColumnSelected(0)) {
					modDialog = new WordModifyDialog(true,selectWord);
				} else {
					JOptionPane.showMessageDialog(c, "단어를 선택해주세요.");
				}
				
				
				
			}
			
			else if (obj == tfSearch) {
				searchWord(tfSearch.getText());
				tfSearch.setText("");

			} else if (obj == btnSearch) {
				searchWord(tfSearch.getText());
				tfSearch.setText("");
			}

			else if (obj == btnAddFavor) {
				// taFavoriteWord.append();

				try {
					int row = tblMainView.getSelectedRow();
					int col = tblMainView.getSelectedColumn();
					String selectMean;
					String selectWord = (String) tblMainView.getValueAt(row, col);

					if (col < 1) {
						selectMean = (String) tblMainView.getValueAt(row, col + 1);
						taFavoriteWord.append("\n" + selectWord + selectMean);

					} else {
						selectMean = (String) tblMainView.getValueAt(row, col - 1);
						taFavoriteWord.append("\n" + selectMean + selectWord);
					}

				} catch (ArrayIndexOutOfBoundsException e1) {
					JOptionPane.showMessageDialog(c, "단어를 선택해주세요");
				}

			} else if (obj == btnDeleteFavor) {
				taFavoriteWord.setText("");
				taFavoriteWord.setText("즐겨 찾기");
			}

			else if (obj == btnDeleteWord) { // 삭제
				delTable();

			} else if (cbWordViewOption.getSelectedIndex() == 0) { // 단어만

				for (int i = 0; i < tblMainView.getRowCount(); i++) {
					tblMainView.setValueAt("", i, 1);

				}

				Vector<VocaInfoVo> data = manager.showWord();
				int j = 0;
				for (VocaInfoVo info : data) {
					String mean = info.getOtherWord();
					tblMainView.setValueAt(mean, j, 0);
					j++;

				}

			} else if (cbWordViewOption.getSelectedIndex() == 1) { // 뜻만

				for (int i = 0; i < tblMainView.getRowCount(); i++) {

					tblMainView.setValueAt("", i, 0);

				}

				Vector<VocaInfoVo> data = manager.showWord();

				int j = 0;
				for (VocaInfoVo info : data) {
					String ko = info.getKoWord();
					tblMainView.setValueAt(ko, j, 1);
					j++;

				}

			} else if (cbWordViewOption.getSelectedIndex() == 2) { // 모두보기

				if (tblMainView.getValueAt(0, 1) == "") {
					Vector<VocaInfoVo> data = manager.showWord();

					int j = 0;
					for (VocaInfoVo info : data) {
						String ko = info.getKoWord();
						tblMainView.setValueAt(ko, j, 1);
						j++;

					}
				} else if (tblMainView.getValueAt(1, 0) == "") {
					Vector<VocaInfoVo> data = manager.showWord();
					int j = 0;
					for (VocaInfoVo info : data) {
						String mean = info.getOtherWord();
						tblMainView.setValueAt(mean, j, 0);
						j++;

					}
				} else if (obj == btnStartGame) {
					
					if(tblMainView.getRowCount()<5) {
						JOptionPane.showMessageDialog(c, "5개 이상의 단어를 입력해주세요.");
					}else {
						gameDialogCreate();
					}
					

				}

			}

		}

	}

	public class PracticeGameRunDialog extends JDialog {

		private JTextField tfInputGameWord = new JTextField(40);
		private JTextField tfGameLife = new JTextField(40);
		private GamePanel pnlGameRun = new GamePanel();
		private JButton btnGameStart = new JButton("게임시작");
		private Container gameC = getContentPane();
		private Thread th;
		private Vector<String> data = getWord();
		private VocaPracticeGameManager gManager = VocaPracticeGameManager.getInstance();
		private GameListener gListener = new GameListener();
		private JTextField resultMessage = new JTextField(10);

		private class GamePanel extends JPanel implements Runnable, ActionListener {

			public GamePanel() {

				setBackground(Color.white);
				setResizable(false);

			}

			@Override
			public void run() {

				try {
					getWord();
					while (true) {
						Thread.sleep(100);

						gameLoseResult();
						gameWinResult();
						decreaseLifeLineOver();
						fillLife();
						gameClose();
						gManager.increaseWordYPos();
						this.repaint();
					}
				} catch (InterruptedException e) {

					// e.printStackTrace();
				}

			}

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub

				super.paintComponent(g);
				g.setColor(Color.black);

				try {
					setFont(new Font("고딕", Font.PLAIN, 25));
					g.drawLine(0, 0, gManager.getLoseLineYPos(), 0);
					g.drawString(data.get(gManager.getIndexTextCount()), gManager.getWordXPos(),
							gManager.getWordYPos());
					g.drawLine(0, gManager.getLoseLineYPos(), 300, gManager.getLoseLineYPos());

				} catch (Exception e) {

				}

			}

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		}

		private void gameLoseResult() {

			if (gManager.getLifeCount().equals(0)) {

				int continueGame = JOptionPane.showConfirmDialog(gameC, "다시하시겠습니까?", "confirm",
						JOptionPane.YES_NO_OPTION);

				if (continueGame == JOptionPane.OK_OPTION) {
					newGameStart();
					gManager.setStageLevel(1);
					setTitle("연습게임" + gManager.getStageLevel() + "단계");
				} else if (continueGame == JOptionPane.NO_OPTION)
					this.dispose();
			}
		}

		private void gameWinResult() {

			if (gManager.getRemainTextCount().equals(0)) {

				int continueGame = JOptionPane.showConfirmDialog(gameC,
						"모든 단어 학습! " + "계속하시겠습니까?\n다음 단계:" + (gManager.getStageLevel() + 1), "confirm",
						JOptionPane.YES_NO_OPTION);

				if (continueGame == JOptionPane.OK_OPTION) {
					newGameStart();
					gManager.upStageLevel();
					setTitle("연습게임" + gManager.getStageLevel() + "단계");
				} else if (continueGame == JOptionPane.NO_OPTION)
					this.dispose();
			}
		}

		private void newGameStart() {
			gManager.gameInit();
			gManager.setRemainTextCount(getWord().size());
			if (th != null && th.isAlive()) {
				th.interrupt();
			}

			th = new Thread(pnlGameRun);
			th.start();
		}

		private class GameListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				String answerWord = manager.compareWord(tfInputGameWord.getText());

				try {

					if (answerWord.equals(getWord().get(gManager.getIndexTextCount()))) {
						System.out.println("정답");
						gManager.increaseIndexTextCount();

						gManager.setWordYPos(0);
						gManager.resetWordXPos();
						gManager.decRemainTextCount();
					}

					tfInputGameWord.setText("");
				} catch (Exception e2) {

					gManager.decreaseLifeCount();

					tfInputGameWord.setText("");
				}

			}

		}

		public PracticeGameRunDialog(boolean modal) {

			setBackground(Color.white);
			setTitle("연습게임" + gManager.getStageLevel() + "단계");
			setVisible(modal);
			setGameUI();
			setGameListener();
			setSize(300, 350);
			newGameStart();

		}

		private void setGameListener() {
			tfInputGameWord.addActionListener(gListener);
			btnGameStart.addActionListener(gListener);
		}

		private void fillLife() {
			String life = "";

			for (int i = 0; i < gManager.getLifeCount(); i++) {

				life += "♥";

			}
			tfGameLife.setText(life);
		}

		private void decreaseLifeLineOver() {
			if (gManager.getWordYPos() >= gManager.getLoseLineYPos()) {
				gManager.decreaseLifeCount();
				gManager.resetWordYPos();
				gManager.resetWordXPos();
				gManager.increaseIndexTextCount();
				gManager.decRemainTextCount();
			}
		}

		private void setGameUI() {

			setGameCenter();
			setGameSouth();

		}

		private void setGameCenter() {
			gameC.add(pnlGameRun, BorderLayout.CENTER);
			gManager.setRemainTextCount(getWord().size());

		}

		private void setGameSouth() {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 1));
			JLabel lblInput = new JLabel("단어입력");
			JLabel lblRemainLife = new JLabel("남은체력");
			panel.add(lblInput);
			panel.add(tfInputGameWord);
			panel.add(lblRemainLife);
			panel.add(tfGameLife);
			// panel.add(new JLabel("레벨 :"+gManager.gets));
			gameC.add(panel, BorderLayout.SOUTH);
		}

		private void gameClose() {
			if (!this.isVisible()) {

				gManager.gameInit();
				if (th != null && th.isAlive())
					th.interrupt();
			}
		}

		private Vector<String> getWord() { // 저장된 단어를 뽑아 meanData에 저장
			Vector<VocaInfoVo> data = manager.showWord();
			Vector<String> meanData = new Vector<String>();

			for (VocaInfoVo info : data) {
				meanData.add(info.getOtherWord());
			}
			return meanData;
		}

	}

	public static void main(String[] args) {
		new VocaBookFrame();

	}

}

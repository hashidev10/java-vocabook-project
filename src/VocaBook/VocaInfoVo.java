package VocaBook;

public class VocaInfoVo {

	
	String otherWord;
	String koWord;
	

	public VocaInfoVo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public VocaInfoVo(String otherWord, String koWord) {
		super();
		this.otherWord = otherWord;
		this.koWord = koWord;
	}


	public String getOtherWord() {
		return otherWord;
	}


	public void setOtherWord(String otherWord) {
		this.otherWord = otherWord;
	}


	public String getKoWord() {
		return koWord;
	}


	public void setKoWord(String koWord) {
		this.koWord = koWord;
	}


	@Override
	public String toString() {
		return "VocaInfoVo [otherWord=" + otherWord + ", koWord=" + koWord + "]";
	}
	
	
}

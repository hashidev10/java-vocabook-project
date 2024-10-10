package VocaBook;

public class VocaPracticeGameManager {

	private Integer fallSpeed = 5;
	private Integer wordYPos = 0;
	private Integer wordXPos = (int) (Math.random() * 200) + 1;
	private Integer loseLineYPos = 280;
	private Integer lifeCount = 5;
	private Integer indexTextCount=0;
	private Integer remainTextCount; 
	private Integer stageLevel=1; 
	
	private static VocaPracticeGameManager instance;

	private VocaPracticeGameManager() {
	}

	public static VocaPracticeGameManager getInstance() {
		if (instance == null) {
			instance = new VocaPracticeGameManager();
		}
		return instance;
	}

	public Integer getFallSpeed() {
		return fallSpeed;
	}

	public void setFallSpeed(Integer fallSpeed) {
		this.fallSpeed = fallSpeed;
	}

	public Integer getWordYPos() {
		return wordYPos;
	}

	public void setWordYPos(Integer wordYPos) {
		this.wordYPos = wordYPos;
	}

	public Integer getLoseLineYPos() {
		return loseLineYPos;
	}

	public void setLoseLineYPos(Integer loseLineYPos) {
		this.loseLineYPos = loseLineYPos;
	}

	public Integer getWordXPos() {
		return wordXPos;
	}

	public void setWordXPos(Integer xPos) {
		this.wordXPos = xPos;
	}

	public Integer getLifeCount() {
		return lifeCount;
	}

	public void setLifeCount(Integer lifeCount) {
		this.lifeCount = lifeCount;
	}
	
	public Integer getIndexTextCount() {
		return indexTextCount;
	}

	public void setIndexTextCount(Integer indexTextCount) {
		this.indexTextCount = indexTextCount;
	}

	
	/**
	 * 게임의 내용 초기화
	 */
	public void gameInit() {
		fallSpeed = 10;
		wordYPos = 0;
		wordXPos = (int) (Math.random() * 200) + 1;
		loseLineYPos = 280;
		lifeCount = 5;
		indexTextCount=0;
	
	}

	/**
	 * 글자의 y좌표를 fallSpeed 만큼 증가시켜 글자를 떨어트림
	 */
	public void increaseWordYPos() {
		wordYPos += fallSpeed;
	}

	public void decreaseLifeCount() {
		lifeCount--;
	}

	public void increaseIndexTextCount() {
		indexTextCount++;
	}
	
	public void resetWordXPos() {
		wordXPos = (int) (Math.random() * 200) + 1;
	}
	
	public void resetWordYPos() {
		wordYPos = 0;
	}

	public Integer getRemainTextCount() {
		return remainTextCount;
	}

	public void decRemainTextCount() {
		remainTextCount--;
	}
	
	public void setRemainTextCount(Integer remainTextCount) {
		this.remainTextCount = remainTextCount;
	}

	public Integer getStageLevel() {
		return stageLevel;
	}

	public void setStageLevel(Integer stageLevel) {
		this.stageLevel = stageLevel;
	}
	
	public void upStageLevel() {
		stageLevel++;
		fallSpeed+=stageLevel*2;
	}

}

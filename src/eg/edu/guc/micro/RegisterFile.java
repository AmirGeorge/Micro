package eg.edu.guc.micro;

public class RegisterFile {

	private static RegisterFile _instance;

	private short r0;
	private short r1;
	private short r2;
	private short r3;
	private short r4;
	private short r5;
	private short r6;
	private short r7;

	private RegisterFile() {

	}

	public static RegisterFile getInstance() {
		if (_instance == null) {
			_instance = new RegisterFile();
		}
		return _instance;
	}

	public short getR0() {
		return r0;
	}

	public void setR0(short r0) {
		this.r0 = r0;
	}

	public short getR1() {
		return r1;
	}

	public void setR1(short r1) {
		this.r1 = r1;
	}

	public short getR2() {
		return r2;
	}

	public void setR2(short r2) {
		this.r2 = r2;
	}

	public short getR3() {
		return r3;
	}

	public void setR3(short r3) {
		this.r3 = r3;
	}

	public short getR4() {
		return r4;
	}

	public void setR4(short r4) {
		this.r4 = r4;
	}

	public short getR5() {
		return r5;
	}

	public void setR5(short r5) {
		this.r5 = r5;
	}

	public short getR6() {
		return r6;
	}

	public void setR6(short r6) {
		this.r6 = r6;
	}

	public short getR7() {
		return r7;
	}

	public void setR7(short r7) {
		this.r7 = r7;
	}
}

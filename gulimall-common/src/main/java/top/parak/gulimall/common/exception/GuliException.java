package top.parak.gulimall.common.exception;

/**
 * 自定义异常
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2020-2-22 20:29:11
 */
public class GuliException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public GuliException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public GuliException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public GuliException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public GuliException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}


}

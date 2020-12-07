package day18;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

public class Computer2 {

	public static class Register {
		
		String name;
		long value = 0;
		
		public Register(String name) {
			this.name = name;
		}
		
	}
	
	public static enum State {RUNNING, WAITING_ON_INPUT};
	
	public static interface Output {
		
		void write(long value);
		
	}
	
	public static interface Input {
		
		boolean hasInput();
		long read();
		
	}
	
	public static class MessageQueue implements Input, Output {
		
		long writerId;
		long readerId;
		Queue<Long> queue = new LinkedList<>();
		
		int writeCount = 0;
		
		public MessageQueue(long writerId, long readerId) {
			this.writerId = writerId;
			this.readerId = readerId;
		}
		
		public void write(long value) {
			System.out.println("Program " + writerId + " wrote to " + readerId + ": " + value);
			queue.add(value);
			writeCount++;
		}
		public boolean hasInput() {
			return !queue.isEmpty();
		}
		public long read() {
			long value = queue.poll();
			System.out.println("Program " + readerId + " read from queue: " + value);
			return value;
		}
		
	}
	
	long programId;
	Map<String, Register> registers = new HashMap<>(26);
	List<String> program;
	int nextCommand = 0;
	
	State state = State.RUNNING;
	Output output;
	Input input;
	
	long lastSoundFrequency;
	
	public Computer2(long programId) {
		this.programId = programId;
		getRegister("p").value = programId;
	}
	
	public void setProgram(List<String> program) {
		this.program = program;
		this.nextCommand = 0;
	}
	
	public Register getRegister(String name) {
		Register register = registers.get(name);
		if (register == null) {
			register = new Register(name);
			registers.put(name, register);
		}
		
		return register;
	}
	
	public void snd(long value) {
		this.output.write(value);
	}
	public void set(String register, long value) {
		getRegister(register).value = value;
	}
	public void add(String register, long value) {
		getRegister(register).value += value;
	}
	public void mul(String register, long value) {
		getRegister(register).value *= value;
	}
	public void mod(String register, long value) {
		getRegister(register).value %= value;
	}
	public void jumpTo(int cmdIndex) {
		this.nextCommand = cmdIndex;
	}
	
	public void runAll() {
		while (step() != null) {
			//continue
		}
	}
	public void runToFirstRecover() {
		String cmd;
		while ((cmd = step()) != null) {
			if (cmd.startsWith("rcv")) {
				String [] parts = cmd.split("\\s+");
				
				long value = getValue(parts[1]);
				if (value != 0)
					break;
			}
		}
	}
	
	public String step() {
		if (nextCommand < 0 || nextCommand >= program.size())
			return null;
		
		String cmdLine = program.get(nextCommand);
		int jumpTo = nextCommand + 1;
		
		String [] parts = cmdLine.split("\\s+");
		String cmd = parts[0];
		if (cmd.equals("snd")) {
			
			snd(getValue(parts[1]));
			
		} else if (cmd.equals("set")) {
			
			set(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("add")) {
			
			add(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("mul")) {
			
			mul(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("mod")) {
			
			mod(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("rcv")) {
			
			if (input.hasInput()) {
				
				getRegister(parts[1]).value = input.read();
				state = State.RUNNING;
				
			} else {
				
				state = State.WAITING_ON_INPUT;
				jumpTo = nextCommand;	//stay on this command
				
			}
			
		} else if (cmd.equals("jgz")) {
			
			long value = getValue(parts[1]);
			if (value > 0)
				jumpTo = nextCommand + (int)getValue(parts[2]);
			
		} else {
			throw new IllegalStateException("Unrecognized command: " + cmd);
		}
		
		this.nextCommand = jumpTo;
		return cmdLine;
	}
	
	/**
	 * Returns the numerical value of the given text. This can be the
	 * value obtained from a register or the value itself.
	 */
	protected long getValue(String value) {
		if (isRegister(value))
			return getRegister(value).value;
		else
			return Integer.parseInt(value);
	}
	
	Pattern REGISTER_PATTERN = Pattern.compile("[a-zA-Z]+");
	
	protected boolean isRegister(String value) {
		return REGISTER_PATTERN.matcher(value).matches();
	}
	
}

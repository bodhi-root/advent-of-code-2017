package day18;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Computer1 {

	public static class Register {
		
		String name;
		long value = 0;
		
		public Register(String name) {
			this.name = name;
		}
		
	}
	
	Map<String, Register> registers = new HashMap<>(26);
	List<String> program;
	int nextCommand = 0;
	
	long lastSoundFrequency;
	
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
	
	public void playSound(long frequency) {
		System.out.println("Play Sound: " + frequency);
		this.lastSoundFrequency = frequency;
	}
	
	public long getLastSoundFrequency() {
		return lastSoundFrequency;
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
	public void recover() {
		System.out.println("Recovered Value: " + lastSoundFrequency);
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
			
			playSound(getValue(parts[1]));
			
		} else if (cmd.equals("set")) {
			
			set(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("add")) {
			
			add(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("mul")) {
			
			mul(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("mod")) {
			
			mod(parts[1], getValue(parts[2]));
			
		} else if (cmd.equals("rcv")) {
			
			long value = getValue(parts[1]);
			if (value != 0)
				recover();
			
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

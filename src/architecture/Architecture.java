package architecture;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import components.Bus;
import components.Demux;
import components.Memory;
import components.Register;
import components.Ula;

public class Architecture {
	
	private boolean simulation; //this boolean indicates if the execution is done in simulation mode.
								//simulation mode shows the components' status after each instruction
	
	
	private boolean halt;
	private Bus extbus1;
	private Bus intbus1;
	private Bus intbus2;
	private Bus statusBus;
	private Memory memory;
	private Memory statusMemory;
	private int memorySize;
	private Register pc;
	private Register ir;
	private Register RPG0;
	private Register RPG1;
	private Register RPG2;
	private Register RPG3;
	private Register stack_top;
	private Register stack_bottom;
	private Register Flags;
	private Ula ula;
	private Demux demux; //only for multiple register purposes
	
	private ArrayList<String> commandsList;
	private ArrayList<Register> registersList;
	
	

	/**
	 * Instanciates all components in this architecture
	 */
	private void componentsInstances() {
		//don't forget the instantiation order
		//buses -> registers -> ula -> memory
		extbus1 = new Bus();
		intbus1 = new Bus();
		intbus2 = new Bus();
		statusBus = new Bus();
		pc = new Register("pc", extbus1, intbus1);
		ir = new Register("ir", extbus1, intbus2);
		RPG0 = new Register("RPG0", extbus1, intbus1);
		RPG1 = new Register ("RPG1", extbus1, intbus1);
		RPG2 = new Register ("RPG2", extbus1, intbus1);
		RPG3 = new Register ("RPG3", extbus1, intbus1);
		stack_top = new Register ("stkTop", extbus1, intbus1);
		stack_bottom = new Register ("stkBottom", extbus1, intbus1);
		Flags = new Register(2, statusBus);
		fillRegistersList();
		ula = new Ula(intbus1, intbus2);
		statusMemory = new Memory(2, intbus2);
		memorySize = 128;
		memory = new Memory(memorySize, extbus1);
		demux = new Demux(); //this bus is used only for multiple register operations
		
		fillCommandsList();
	}

	/**
	 * This method fills the registers list inserting into them all the registers we have.
	 * IMPORTANT!
	 * The first register to be inserted must be the default RPG0
	 */
	private void fillRegistersList() {
		registersList = new ArrayList<Register>();
		registersList.add(RPG0);
		registersList.add(RPG1);
		registersList.add(RPG2);
		registersList.add(RPG3);
		registersList.add(stack_top);
		registersList.add(stack_bottom);
		registersList.add(pc);
		registersList.add(ir);
		registersList.add(Flags);
	}

	/**
	 * Constructor that instanciates all components according the architecture diagram
	 */
	public Architecture() {
		componentsInstances();
		
		//by default, the execution method is never simulation mode
		simulation = false;
	}

	
	public Architecture(boolean sim) {
		componentsInstances();
		
		//in this constructor we can set the simoualtion mode on or off
		simulation = sim;
	}



	//getters
	
	protected Bus getExtbus1() {
		return extbus1;
	}

	protected Bus getIntbus1() {
		return intbus1;
	}

	protected Bus getIntbus2() {
		return intbus2;
	}

	protected Memory getMemory() {
		return memory;
	}

	protected Register getPc() {
		return pc;
	}

	protected Register getIr() {
		return ir;
	}

	protected Register getRPG0() {
		return RPG0;
	}
	
	protected Register getRPG1() {
		return RPG1;
	}
	
	protected Register getRPG2() {
		return RPG2;
	}
	
	protected Register getRPG3() {
		return RPG3;
	}
	
	protected Register getStackTop() {
		return stack_top;
	}
	
	protected Register getStackBottom() {
		return stack_bottom;
	}

	protected Register getFlags() {
		return Flags;
	}
	
	protected Demux getDemux() {
		return demux;
	}

	protected Ula getUla() {
		return ula;
	}

	public ArrayList<String> getCommandsList() {
		return commandsList;
	}



	//all the microprograms must be impemented here
	//the instructions table is
	/*
	 *
			add addr (RPG0 <- RPG0 + addr)
			sub addr (RPG0 <- RPG0 - addr)
			jmp addr (pc <- addr)
			jz addr  (se bitZero pc <- addr)
			jn addr  (se bitneg pc <- addr)
			read addr (RPG0 <- addr)
			store addr  (addr <- RPG0)
			ldi x    (RPG0 <- x. x must be an integer)
			inc    (RPG0++)
			move regA regB (regA <- regB)
	 */
	
	/**
	 * This method fills the commands list arraylist with all commands used in this architecture
	 */
	protected void fillCommandsList() {
		commandsList = new ArrayList<String>();
		commandsList.add("addRegReg");   //0
		commandsList.add("addMemReg");   //1
		commandsList.add("addRegMem");   //2
		commandsList.add("subRegReg");   //3
		commandsList.add("subMemReg");   //4
		commandsList.add("subRegMem");   //5
		commandsList.add("moveMemReg");  //6
		commandsList.add("moveRegMem");  //7
		commandsList.add("moveRegReg");  //8
		commandsList.add("moveImmReg");  //9
		commandsList.add("incReg");      //10
		commandsList.add("incMem");      //11
		commandsList.add("jmp");         //12
		commandsList.add("jn");          //13
		commandsList.add("jz");          //14
		commandsList.add("jnz");         //15
		commandsList.add("jeq");         //16
		commandsList.add("jgt");         //17
		commandsList.add("jlw");         //18
		commandsList.add("call");        //19
		commandsList.add("ret");         //20
	}

	
	/**
	 * This method is used after some ULA operations, setting the flags bits according the result.
	 * @param result is the result of the operation
	 * NOT TESTED!!!!!!!
	 */
	private void setStatusFlags(int result) {
		Flags.setBit(0, 0);
		Flags.setBit(1, 0);
		if (result==0) { //bit 0 in flags must be 1 in this case
			Flags.setBit(0,1);
		}
		if (result<0) { //bit 1 in flags must be 1 in this case
			Flags.setBit(1,1);
		}
	}

	//LIBERTADORES
	public void addRR() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(1);
		
		//operação
		ula.add(); //the result is in the second ula's internal register
		ula.read(1); //the operation result is in the internalbus 2
		setStatusFlags(intbus1.get()); //changing flags due the end of the operation
		
		registersInternalStore();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//DA
	public void addMR() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(1);
		
		//operação
		ula.add(); //the result is in the second ula's internal register
		ula.read(1); //the operation result is in the internalbus 2
		setStatusFlags(intbus1.get()); //changing flags due the end of the operation
		
		registersInternalStore();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//CAMPEAO
	public void addRM() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(1);
		
		//operação
		ula.add(); //the result is in the second ula's internal register
		ula.internalRead(1);
		ir.internalStore();
		setStatusFlags(intbus2.get()); //changing flags due the end of the operation
		
		//armazena na memória
		pc.read();
		memory.read();
		memory.store();
		ir.read();
		memory.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//FLUMINENSE
	public void subRR() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(1);
		
		//operação
		ula.sub(); //the result is in the second ula's internal register
		ula.read(1); //the operation result is in the internalbus 2
		setStatusFlags(intbus1.get()); //changing flags due the end of the operation
		
		registersInternalStore();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//joia
	public void subMR() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(1);
		
		//operação
		ula.sub(); //the result is in the second ula's internal register
		ula.read(1); //the operation result is in the internalbus 2
		setStatusFlags(intbus1.get()); //changing flags due the end of the operation
		
		registersInternalStore();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//joia
	public void subRM() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//primeiro parametro
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		//segundo parametro
		pc.read();
		memory.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(1);
		
		//operação
		ula.sub(); //the result is in the second ula's internal register
		ula.internalRead(1);
		ir.internalStore();
		setStatusFlags(intbus2.get()); //changing flags due the end of the operation
		
		//armazena na memória
		pc.read();
		memory.read();
		memory.store();
		ir.read();
		memory.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//joia
	public void moveMR() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read(); 
		memory.read();
		memory.read();
		ir.store();
	
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the second parameter (the second reg id)
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		ir.read();
		registersStore(); //starts the read from the register identified into demux bus
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the next instruction. We go back to the FETCH status.
	}
	
	//supimpa
	public void moveRM() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersRead();
		ir.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the second parameter (the second reg id)
		
		pc.read();
		memory.read();
		memory.store();
		ir.read();
		memory.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the next instruction. We go back to the FETCH status.
}
	
	public void moveRR() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersRead();
		ir.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the second parameter (the second reg id)
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		ir.read();
		registersStore(); //starts the read from the register identified into demux bus
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the next instruction. We go back to the FETCH status.
	}

	//ALL RIGHT
	public void moveIR() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		ir.store();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the second parameter (the second reg id)
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		ir.read();
		registersStore(); //starts the read from the register identified into demux bus
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the next instruction. We go back to the FETCH status.
			
	}
	
	//totalmente funcionivis
	public void IncR() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get()); //points to the correct register
		registersInternalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		registersInternalStore();
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}
	
	//宝石
	public void IncM() {
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(1);
		ula.inc();
		ula.internalRead(1);
		ir.internalStore();
		pc.read();
		memory.read();
		memory.store();
		ir.read();
		memory.store();
		
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
	}

	public void jmp() {
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore(); //now pc points to the parameter address
		pc.read();
		memory.read();
		pc.store();
	}
	
	public void jn() {
		
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();//now pc points to the parameter address
		
		pc.read();
		memory.read();// now the parameter value (address of the jz) is in the external bus
		ir.store();
		ir.internalRead();
		statusMemory.storeIn1(); //the address is in position 1 of the status memory
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn0();
		intbus2.put(Flags.getBit(1)); //the ZERO bit is in the external bus 
		statusMemory.read(); //gets the correct address (next instruction or parameter address)
		ir.internalStore();
		ir.read();
		pc.store(); //stores into pc
	}
	
	public void jz() {
		
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();//now pc points to the parameter address
		
		pc.read();
		memory.read();// now the parameter value (address of the jz) is in the external bus
		ir.store();
		ir.internalRead();
		statusMemory.storeIn1(); //the address is in position 1 of the status memory
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn0();
		intbus2.put(Flags.getBit(0)); //the ZERO bit is in the external bus 
		statusMemory.read(); //gets the correct address (next instruction or parameter address)
		ir.internalStore();
		ir.read();
		pc.store(); //stores into pc
	}
	
	public void jnz() {
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();//now pc points to the parameter address
		
		pc.read();
		memory.read();// now the parameter value (address of the jz) is in the external bus
		ir.store();
		ir.internalRead();
		statusMemory.storeIn0(); //the address is in position 1 of the status memory
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn1();
		intbus2.put(Flags.getBit(0)); //the ZERO bit is in the external bus 
		statusMemory.read(); //gets the correct address (next instruction or parameter address)
		ir.internalStore();
		ir.read();
		pc.store(); //stores into pc
	}
	
	public void jeq() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(1);
		
		ula.sub();
		ula.read(1);
		setStatusFlags(intbus1.get());
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		ir.store();
		ir.internalRead();
		statusMemory.storeIn1();
			
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn0();
		
		intbus2.put(Flags.getBit(0));
		statusMemory.read();
		
		ir.internalStore();
		ir.read();
		
		pc.store();
	}
	
	public void jgt() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(1);
		
		ula.sub();
		ula.read(1);
		setStatusFlags(intbus1.get());
		
		intbus1.put(Flags.getBit(0));
		ula.store(0);
		
		intbus1.put(Flags.getBit(1));
		ula.store(1);
		
		
		ula.add();
		ula.read(1);
		setStatusFlags(intbus1.get());
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		ir.store();
		ir.internalRead();
		statusMemory.storeIn1();
			
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn0();
		
		intbus2.put(Flags.getBit(0));
		statusMemory.read();
		
		ir.internalStore();
		ir.read();
		
		pc.store();
	}	
	
	public void jlw() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(0);
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		demux.setValue(extbus1.get());
		registersInternalRead();
		ula.store(1);
		
		ula.sub();
		ula.read(1);
		setStatusFlags(intbus1.get());
		
		intbus1.put(Flags.getBit(0));
		ula.store(0);
		
		intbus1.put(Flags.getBit(1));
		ula.store(1);
		
		ula.sub();
		ula.read(1);
		setStatusFlags(intbus1.get());
		
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		ir.store();
		ir.internalRead();
		statusMemory.storeIn1();
			
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.internalRead(1);
		statusMemory.storeIn0();
		
		intbus2.put(Flags.getBit(1));
		statusMemory.read();
		
		ir.internalStore();
		ir.read();
		
		pc.store();
	}
	
	public void call() {
		//pc++
		pc.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		pc.internalStore();
		
		pc.read();
		memory.read();
		ir.store();
		ir.internalRead();
		ula.internalStore(0);
		
		stack_top.read();
		memory.store();
		
		ula.inc();
		ula.internalRead(1);
		ir.internalStore();
		ir.read();
		memory.store();
		
		ula.read(0);
		pc.internalStore();
		
		stack_top.internalRead();
		ula.store(0);
		intbus1.put(1);
		ula.store(1);
		ula.sub();
		ula.read(1);
		stack_top.internalStore();
	}
	
	public void ret() {
		stack_top.internalRead();
		ula.store(1);
		ula.inc();
		ula.read(1);
		stack_top.internalStore();
		
		stack_top.read();
		memory.read();
		pc.store();
	}
	
	public ArrayList<Register> getRegistersList() {
		return registersList;
	}

	/**
	 * This method performs an (external) read from a register into the register list.
	 * The register id must be in the demux bus
	 */
	protected void registersRead() {
		registersList.get(demux.getValue()).read();
	}
	
	/**
	 * This method performs an (internal) read from a register into the register list.
	 * The register id must be in the demux bus
	 */
	protected void registersInternalRead() {
		registersList.get(demux.getValue()).internalRead();;
	}
	
	/**
	 * This method performs an (external) store toa register into the register list.
	 * The register id must be in the demux bus
	 */
	protected void registersStore() {
		registersList.get(demux.getValue()).store();
	}
	
	/**
	 * This method performs an (internal) store toa register into the register list.
	 * The register id must be in the demux bus
	 */
	protected void registersInternalStore() {
		registersList.get(demux.getValue()).internalStore();;
	}



	/**
	 * This method reads an entire file in machine code and
	 * stores it into the memory
	 * NOT TESTED
	 * @param filename
	 * @throws IOException 
	 */
	public void readExec(String filename) throws IOException {
		   BufferedReader br = new BufferedReader(new		 
		   FileReader(filename+".dxf"));
		   String linha;
		   int i=0;
		   while ((linha = br.readLine()) != null) {
			     extbus1.put(i);
			     memory.store();
			   	 extbus1.put(Integer.parseInt(linha));
			     memory.store();
			     i++;
			}
			br.close();
	}
	
	/**
	 * This method executes a program that is stored in the memory
	 */
	public void controlUnitEexec() {
		halt = false;
		while (!halt) {
			fetch();
			decodeExecute();
		}

	}
	

	/**
	 * This method implements The decode proccess,
	 * that is to find the correct operation do be executed
	 * according the command.
	 * And the execute proccess, that is the execution itself of the command
	 */
	private void decodeExecute() {
		ir.internalRead(); //the instruction is in the internalbus2
		int command = intbus2.get();
		simulationDecodeExecuteBefore(command);
		switch (command) {
		case 0:
			addRR();
			break;
		case 1:
			addMR();
			break;
		case 2:
			addRM();
			break;
		case 3:
			subRR();
			break;
		case 4:
			subMR();
			break;
		case 5:
			subRM();
			break;
		case 6:
			moveMR();
			break;
		case 7:
			moveRM();
			break;
		case 8:
			moveRR();
			break;
		case 9:
			moveIR();
			break;
		case 10:
			IncR();
			break;
		case 11:
			IncM();
			break;
		case 12:
			jmp();
			break;
		case 13:
			jn();
			break;
		case 14:
			jz();
			break;
		case 15:
			jnz();
			break;
		case 16:
			jeq();
			break;
		case 17:
			jgt();
			break;
		case 18:
			jlw();
			break;
		case 19:
			call();
			break;
		case 20:
			ret();
			break;
		default:
			halt = true;
			break;
		}
		if (simulation)
			simulationDecodeExecuteAfter();
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED
	 * @param command 
	 */
	private void simulationDecodeExecuteBefore(int command) {
		System.out.println("----------BEFORE Decode and Execute phases--------------");
		String instruction;
		int parameter = 0;
		for (Register r:registersList) {
			System.out.println(r.getRegisterName()+": "+r.getData());
		}
		if (command !=-1)
			instruction = commandsList.get(command);
		else
			instruction = "END";
		if (hasOperands(instruction)) {
			parameter = memory.getDataList()[pc.getData()+1];
			System.out.println("Instruction: "+instruction+" "+parameter);
		}
		else
			System.out.println("Instruction: "+instruction);
		if ("read".equals(instruction))
			System.out.println("memory["+parameter+"]="+memory.getDataList()[parameter]);
		
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED 
	 */
	private void simulationDecodeExecuteAfter() {
		String instruction;
		System.out.println("-----------AFTER Decode and Execute phases--------------");
		System.out.println("Internal Bus 1: "+intbus1.get());
		System.out.println("Internal Bus 2: "+intbus2.get());
		System.out.println("External Bus 1: "+extbus1.get());
		for (Register r:registersList) {
			System.out.println(r.getRegisterName()+": "+r.getData());
		}
		Scanner entrada = new Scanner(System.in);
		System.out.println("Press <Enter>");
		String mensagem = entrada.nextLine();
	}

	/**
	 * This method uses pc to find, in the memory,
	 * the command code that must be executed.
	 * This command must be stored in ir
	 * NOT TESTED!
	 */
	private void fetch() {
		pc.read();
		memory.read();
		ir.store();
		simulationFetch();
	}

	/**
	 * This method is used to show the components status in simulation conditions
	 * NOT TESTED!!!!!!!!!
	 */
	private void simulationFetch() {
		if (simulation) {
			System.out.println("-------Fetch Phase------");
			System.out.println("pc: "+pc.getData());
			System.out.println("ir: "+ir.getData());
		}
	}

	/**
	 * This method is used to show in a correct way the operands (if there is any) of instruction,
	 * when in simulation mode
	 * NOT TESTED!!!!!
	 * @param instruction 
	 * @return
	 */
	private boolean hasOperands(String instruction) {
		if ("inc".equals(instruction)) //inc is the only one instruction having no operands
			return false;
		else
			return true;
	}

	/**
	 * This method returns the amount of positions allowed in the memory
	 * of this architecture
	 * NOT TESTED!!!!!!!
	 * @return
	 */
	public int getMemorySize() {
		return memorySize;
	}
	
	public static void main(String[] args) throws IOException {
		Architecture arch = new Architecture(true);
		arch.readExec("program");
		arch.controlUnitEexec();
	}
	

}

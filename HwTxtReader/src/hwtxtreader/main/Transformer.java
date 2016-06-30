package hwtxtreader.main;

import hwtxtreader.bean.Txterror;

public interface Transformer {

	public void PostResult(Boolean t);

	public void PostError(Txterror txterror);	


}

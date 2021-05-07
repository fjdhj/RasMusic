package fr.fjdhj.alarmclock;

import fr.fjdhj.rasmusic.module.ModuleDefault;
import fr.fjdhj.rasmusic.templates.HTTPTemplates;
import fr.fjdhj.rasmusic.webserv.HTTPResponse;

public class AlarmClock implements ModuleDefault {

	@Override
	public String getModuleName() {
		return "AlarmClock";
	} 

	@Override
	public int getModuleVersion() {
		return 0;
	}

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public HTTPResponse GETRequest(String[] arg0) {
		// TODO Auto-generated method stub
		return HTTPTemplates.error404();
	}

}

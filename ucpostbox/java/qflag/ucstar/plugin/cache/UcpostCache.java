package qflag.ucstar.plugin.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import qflag.ucstar.plugin.ucpostbox.pojo.UcpostExchangePoji;

public class UcpostCache {
	private static UcpostCache instance = null;
	private Map<String, List<UcpostExchangePoji>> exchangeMailCache = null;

	private UcpostCache() {
		_init();
	}

	private void _init() {
		exchangeMailCache = new ConcurrentHashMap<String, List<UcpostExchangePoji>>();
	}

	public synchronized static UcpostCache getInstance() {
		if (instance == null) {
			instance = new UcpostCache();
		}
		return instance;
	}

	public void removeExchangeMail(String _username) {
		this.exchangeMailCache.remove(_username);
	}

	public List<UcpostExchangePoji> getExhangeMail(String _username) {
		return this.exchangeMailCache.get(_username);
	}

	public void putExchangeMail(String _username,
			List<UcpostExchangePoji> _mails) {
		if (_mails != null) {
			this.exchangeMailCache.put(_username, _mails);
		}
	}

}

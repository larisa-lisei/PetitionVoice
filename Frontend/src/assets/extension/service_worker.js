const API_URL = "http://localhost:8079/api/petitions/petition-of-the-day";
const CACHE_KEY = "petitionOfTheDay";

async function fetchAndCache() {
  try{
    const res = await fetch(API_URL);

    //no content
    if (res.status === 204) {
      await chrome.storage.local.set({ [CACHE_KEY]: null, updatedAt: Date.now() });
      return;
    }

    if (!res.ok) throw new Error(`HTTP ${res.status}`);

    //there is a petition of the day
    const data = await res.json();
    await chrome.storage.local.set({ [CACHE_KEY]: data, updatedAt: Date.now() });
  } catch(e) {
    //handle promise
    await chrome.storage.local.set({
      lastFetchError: String(e),
      lastFetchErrorAt: Date.now(),
    });
  }
}

//startup
function ensureAlarmAndFetch() {
  chrome.alarms.create("refreshPetition", { periodInMinutes: 5 });
  fetchAndCache();
}

chrome.runtime.onInstalled.addListener(ensureAlarmAndFetch);
chrome.runtime.onStartup.addListener(ensureAlarmAndFetch);

//every 5 minutes
chrome.alarms.onAlarm.addListener((alarm) => {
  if (alarm.name === "refreshPetition") fetchAndCache();
});

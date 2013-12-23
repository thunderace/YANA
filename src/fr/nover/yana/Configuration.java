/**
*Cette application a �t� d�velopp�e par Nicolas -Nover- Guilloux.
*Elle a �t� cr��e afin d'interagir avec YANA, lui-m�me cr�� par Idleman.
*Trouvez les travaux d'Idleman ici : http://blog.idleman.fr/?p=1788
*Vous pouvez me contacter � cette adresse : Etsu@live.fr
**/

package fr.nover.yana;

import fr.nover.yana.R;
import fr.nover.yana.installWizard.Assistant_Installation;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class Configuration extends PreferenceActivity {

	String [] testValues = {"Mademoiselle" , "Madame", "Monsieur"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.option);

    	final Intent SetupWizard = new Intent(this, Assistant_Installation.class);
    	
    	ListPreference lp = (ListPreference) findPreference("sexe");
		lp.setEntries(testValues);
		lp.setEntryValues(testValues);
    	
		Preference button = (Preference)findPreference("button");
		button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                @Override
		                public boolean onPreferenceClick(Preference arg0) {
		            		startActivity(SetupWizard);
		            		finish();
		                    return true;
		                }
		            });
	}
}
package org.golde.puzzlesafari.feature.archery;

import org.golde.puzzlesafari.feature.FeatureBase;
import org.golde.puzzlesafari.utils.NMSUtils;
import org.golde.puzzlesafari.utils.NMSUtils.Type;

public class FeatureArchery extends FeatureBase {

	@Override
	public void onEnable() {
		NMSUtils.registerEntity("custom_sheep", Type.SHEEP, CustomSheep.class, false);
	}
	
}

/**
 *
 *
 * Copyright (c) 2012 eZuce, Inc. All rights reserved.
 * Contributed to SIPfoundry under a Contributor Agreement
 *
 * This software is free software; you can redistribute it and/or modify it under
 * the terms of the Affero General Public License (AGPL) as published by the
 * Free Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 */
package org.sipfoundry.sipxconfig.phone.polycom;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.sipfoundry.sipxconfig.device.ProfileContext;
import org.sipfoundry.sipxconfig.phone.polycom.DirectoryConfiguration.PolycomPhonebookEntry;
import org.sipfoundry.sipxconfig.speeddial.Button;
import org.sipfoundry.sipxconfig.speeddial.SpeedDial;

/**
 * Responsible for generating ipmid.cfg
 */
public class FeaturesConfiguration extends ProfileContext {
    private final static int MAX_BLF_LINE = 6;
    
    private List<Button> m_buttons;
    private PolycomPhone m_phone;
    
    public FeaturesConfiguration(PolycomPhone device) {
        super(device, device.getTemplateDir() + "/features.cfg.vm");
        this.m_phone = device;
    }
    
    public FeaturesConfiguration(PolycomPhone device, SpeedDial speedDial) {
        this(device);
        if (speedDial != null) {
            m_buttons = speedDial.getButtons();
        }
    }
    
    public Collection<PolycomPhonebookEntry> getRows() {
        if (m_buttons != null && !m_buttons.isEmpty()) {
            Collection<PolycomPhonebookEntry> polycomEntries = new LinkedHashSet<PolycomPhonebookEntry>(m_buttons.size());
            return transformSpeedDial(m_buttons, polycomEntries);
        }
        
        return Collections.emptyList();
    }
    
    private Collection<PolycomPhonebookEntry> transformSpeedDial(List<Button> buttons, Collection<PolycomPhonebookEntry> polycomEntries) {
        int maxLine = MAX_BLF_LINE;
        if(m_phone != null && m_phone.getModel() != null) {
            maxLine = m_phone.getModel().getMaxLineCount();
        }
        
        for (int i = 0, index = 0; i < buttons.size() && index < maxLine; i++) {
            Button button = buttons.get(i);
            if (button.isBlf()) {
                polycomEntries.add(new PolycomPhonebookEntry(button, ++index));
            }
        }
        
        return polycomEntries;
    }
}

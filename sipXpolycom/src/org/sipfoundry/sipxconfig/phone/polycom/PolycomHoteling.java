package org.sipfoundry.sipxconfig.phone.polycom;

import static java.lang.String.format;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sipfoundry.sipxconfig.common.User;
import org.sipfoundry.sipxconfig.device.Device;
import org.sipfoundry.sipxconfig.device.Profile;
import org.sipfoundry.sipxconfig.device.ProfileLocation;
import org.sipfoundry.sipxconfig.phone.Phone;
import org.sipfoundry.sipxconfig.phone.PhoneContext;
import org.sipfoundry.sipxconfig.device.Profile;
import org.sipfoundry.sipxconfig.device.ProfileContext;
import org.sipfoundry.sipxconfig.device.ProfileFilter;
import org.sipfoundry.sipxconfig.phone.PhoneModel;
import org.sipfoundry.sipxconfig.hoteling.HotelingManager;
import org.sipfoundry.sipxconfig.setting.Group;

public class PolycomHoteling implements HotelingManager {
    private static final Log LOG = LogFactory.getLog(PhoneUpdateResource.class);

    private ProfileLocation m_defaultProfileLocation;
    private PhoneContext m_phoneContext;
    private PhoneModel m_phoneBaseModel;

    /**
     * generate the necessary files for the user.
     * @param user
     */
    public void generate(User user) {
        PolycomPhone phone = (PolycomPhone) m_phoneContext.newPhone(m_phoneBaseModel);
        HotelingProfile profile = new HotelingProfile(getHotelingFilename(user));
        profile.generate(phone, m_defaultProfileLocation);
    }

    /**
     * generate the necessary files for the group.
     * @param user
     */
    public void generate(Group g) {
        // TODO will implement in phase #2
    }

    void remove(User user) {
        m_defaultProfileLocation.removeProfile(getHotelingFilename(user));
    }

    public String getHotelingFilename(User user) {
        return format("%s.cfg", user.getUserName());
    }

    public void setDefaultProfileLocation(ProfileLocation defaultProfileLocation) {
        m_defaultProfileLocation = defaultProfileLocation;
    }

    public ProfileLocation getDefaultProfileLocation() {
        return m_defaultProfileLocation;
    }

    public void setPhoneContext(PhoneContext phoneContext) {
        m_phoneContext = phoneContext;
    }

    public void setPhoneBaseModel(PhoneModel phoneModel) {
        m_phoneBaseModel = phoneModel;
    }

    static class HotelingProfile extends Profile {
        public HotelingProfile(String name) {
            super(name, PolycomPhone.MIME_TYPE_PLAIN);
        }

        @Override
        protected ProfileFilter createFilter(Device device) {
            return null;
        }

        @Override
        protected ProfileContext createContext(Device device) {
            PolycomPhone phone = (PolycomPhone) device;
            return new HotelingConfiguration(phone);
        }
    }
}

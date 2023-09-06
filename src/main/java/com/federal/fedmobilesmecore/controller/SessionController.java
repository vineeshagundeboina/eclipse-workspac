/*
 * package com.federal.fedmobilesmecore.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.federal.fedmobilesmecore.dto.User; import
 * com.federal.fedmobilesmecore.model.APIResponse; import
 * com.federal.fedmobilesmecore.model.DestorySession; import
 * com.federal.fedmobilesmecore.model.MobileUserSessionModel; import
 * com.federal.fedmobilesmecore.model.RefreshTokenmodel; import
 * com.federal.fedmobilesmecore.model.SMEMessage; import
 * com.federal.fedmobilesmecore.model.WebUserSessionModel; import
 * com.federal.fedmobilesmecore.repository.UserRepository; import
 * com.federal.fedmobilesmecore.service.CustomUserDetailsService;
 * 
 * @RestController
 * 
 * @RequestMapping("/core/auth") public class SessionController {
 * 
 * @Autowired UserRepository userRepository;
 * 
 * @Autowired CustomUserDetailsService userdetails;
 * 
 * @PostMapping("/mobile_user_session") public ResponseEntity<SMEMessage>
 * createMobileSession(@RequestBody MobileUserSessionModel request) {
 * System.out.println(request.getMpin()); SMEMessage apiResponse =
 * userdetails.generateMobileUserAuthToken(request); return
 * ResponseEntity.ok(apiResponse); }
 * 
 * @PostMapping("/web_user_session") public ResponseEntity<SMEMessage>
 * createWebUsersSession(@RequestBody WebUserSessionModel request) {
 * System.out.println("request" + request.getPrefNo()); SMEMessage apiResponse =
 * userdetails.generateWebUserAuthToken(request); return
 * ResponseEntity.ok(apiResponse);
 * 
 * }
 * 
 * @DeleteMapping("/destory_mobile_session") public ResponseEntity<SMEMessage>
 * destoryMobileUserSession(@RequestBody DestorySession request) { SMEMessage
 * apiResponse = userdetails.destorySession(request); return
 * ResponseEntity.ok(apiResponse); }
 * 
 * @DeleteMapping("/destory_web_session") public ResponseEntity<SMEMessage>
 * destoryWebUserSession(@RequestBody DestorySession request) { SMEMessage
 * apiResponse = userdetails.destorySession(request); return
 * ResponseEntity.ok(apiResponse); }
 * 
 * @GetMapping("/load_pref_no/{prefNo}") public ResponseEntity<SMEMessage>
 * loadUser(@PathVariable String prefNo) { SMEMessage message = new
 * SMEMessage(); message.setStatus(false); User apiResponse =
 * userdetails.loadUserByPrefNO(prefNo); if (apiResponse != null) { if
 * (apiResponse.getPrefNo() != null) { message.setStatus(true); } } return
 * ResponseEntity.ok(message); }
 * 
 * 
 * @PostMapping("/refreshtoken") public ResponseEntity<SMEMessage>
 * refresh(@RequestBody RefreshTokenmodel apptoken) {
 * System.out.println("request" + apptoken); SMEMessage apiResponse =
 * userdetails.refreshToken(apptoken.getAppToken()); return
 * ResponseEntity.ok(apiResponse);
 * 
 * }
 * 
 * 
 * }
 */
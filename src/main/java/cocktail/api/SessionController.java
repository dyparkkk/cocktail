package cocktail.api;

//@Slf4j
//@Api(tags = "session")
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(value = "/api/v1/session")
//public class SessionController {
//
//    @GetMapping("/session")
//    @ApiOperation(value = "세션", notes = "JSESSIONID 확인")
//    public SessionInfoResponse sessionInfo(HttpServletRequest req) {
//
//        HttpSession session = req.getSession(false);
//
//        SessionDto user = (SessionDto) session.getAttribute("LOGIN_MEMBER");
//        if (user == null) {
//            throw new IllegalStateException("JSESSIONID를 확인할 수 없습니다.");
//        }
//
//        return new SessionInfoResponse((String) session.getAttribute(LOGIN_MEMBER),
//                session.getId(),
//                new Date( session.getCreationTime()),
//                new Date(session.getLastAccessedTime()));
//    }
//}

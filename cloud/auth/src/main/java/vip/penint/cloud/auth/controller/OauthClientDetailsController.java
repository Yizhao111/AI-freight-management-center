package vip.penint.cloud.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.penint.cloud.auth.entity.OauthClientDetails;
import vip.penint.cloud.auth.service.OauthClientDetailsService;
import vip.penint.cloud.common.core.entity.AjaxResult;
import vip.penint.cloud.common.core.entity.QueryRequest;
import vip.penint.cloud.common.core.exception.PenintException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 *
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("client")
public class OauthClientDetailsController {

    private final OauthClientDetailsService oauthClientDetailsService;

    @GetMapping("check/{clientId}")
    public AjaxResult checkUserName(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        return AjaxResult.success(client == null);
    }

    @GetMapping("secret/{clientId}")
    @PreAuthorize("hasAuthority('client:decrypt')")
    public AjaxResult getOriginClientSecret(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        String origin = client != null ? client.getOriginSecret() : StringUtils.EMPTY;
        return AjaxResult.success(origin);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('client:view')")
    public AjaxResult oauthCliendetailsList(QueryRequest request, OauthClientDetails oAuthClientDetails) {
        return AjaxResult.success(this.oauthClientDetailsService.findOauthClientDetails(request, oAuthClientDetails));
    }


    @PostMapping
    @PreAuthorize("hasAuthority('client:add')")
    public AjaxResult addOauthCliendetails(@Valid @RequestBody OauthClientDetails oAuthClientDetails) throws PenintException {
        try {
            this.oauthClientDetailsService.createOauthClientDetails(oAuthClientDetails);
            return AjaxResult.success();
        } catch (Exception e) {
            String message = "新增客户端失败";
            log.error(message, e);
            throw new PenintException(message);
        }
    }

    @DeleteMapping("/{clientIds}")
    @PreAuthorize("hasAuthority('client:delete')")
    public AjaxResult deleteOauthCliendetails(@PathVariable String[] clientIds) throws PenintException {
        try {
            this.oauthClientDetailsService.deleteOauthClientDetails(clientIds);
            return AjaxResult.success();
        } catch (Exception e) {
            String message = "删除客户端失败";
            log.error(message, e);
            throw new PenintException(message);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    public AjaxResult updateOauthCliendetails(@Valid @RequestBody OauthClientDetails oAuthClientDetails) throws PenintException {
        try {
            this.oauthClientDetailsService.updateOauthClientDetails(oAuthClientDetails);
            return AjaxResult.success();
        } catch (Exception e) {
            String message = "修改客户端失败";
            log.error(message, e);
            throw new PenintException(message);
        }
    }
}

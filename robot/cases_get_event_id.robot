*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Test Setup    Criar Sessao API

*** Variables ***
${TAG_ALL_TESTS}    smoke    regression
${TAG_SUCCESS}      success
${TAG_FAILURE}      failure
${TAG_GET}          get

*** Test Cases ***
GET-EVENT-ID-001 - Validar obtenção de evento por ID existente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar obtenção de evento por ID existente

    ${event_id}    ${ticket_type_id}=    Criar Evento e Ticket Type
    Executar Get Event By ID    ${event_id}    ${STATUS_200}

GET-EVENT-ID-002 - Validar impossibilidade de obtenção de evento por ID inexistente
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar impossibilidade de obtenção de evento por ID inexistente

    Executar Get Event By ID    ${uuid_inexistente}    ${STATUS_404}

GET-EVENT-ID-003 - Validar impossibilidade de obtenção de evento por ID em formato inválido
    [Tags]    ${TAG_ALL_TESTS}    ${TAG_SUCCESS}    ${TAG_GET}
    [Documentation]    Validar impossibilidade de obtenção de evento por ID em formato inválido

    Executar Get Event By ID    ${uuid_invalido}    ${STATUS_400}